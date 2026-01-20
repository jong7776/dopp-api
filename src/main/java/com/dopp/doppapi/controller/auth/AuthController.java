package com.dopp.doppapi.controller.auth;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.response.ApiResultCode;
import com.dopp.doppapi.config.properties.CookieProperties;
import com.dopp.doppapi.dto.auth.LoginRequest;
import com.dopp.doppapi.dto.auth.RefreshTokenDto;
import com.dopp.doppapi.dto.user.UserDto;
import com.dopp.doppapi.security.JwtUtil;
import com.dopp.doppapi.service.auth.AuthService;
import com.dopp.doppapi.service.auth.RefreshTokenService;
import com.dopp.doppapi.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    private final CookieProperties cookieProperties;
    private final UserService userService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "로그인", description = "로그인 ID와 비밀번호를 사용하여 액세스 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResult<Map<String, String>>> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse response) {

        UserDto user = userService.getUserInfoByLoginId(request.getLoginId());
        if (authService.isPasswordValid(user, request.getPassword())) {
            return ResponseEntity.status(401).body(ApiResult.fail(ApiResultCode.INVALID_PW));
        }

        if (authService.isUserActiveValid(user)) {
            return ResponseEntity.status(401).body(ApiResult.fail(ApiResultCode.INVALID_USER));
        }

        String accessToken = jwtUtil.generateAccessToken(user.getLoginId(), "ROLE_USER");
        String refreshToken = jwtUtil.generateRefreshToken(user.getLoginId());

        // DB에 Refresh Token 저장
        String userAgent = httpRequest.getHeader("User-Agent");
        String ipAddress = httpRequest.getRemoteAddr();
        refreshTokenService.createRefreshToken(user.getUserId(), refreshToken, userAgent, ipAddress);

        // Refresh Token을 HttpOnly Cookie에 저장 (ResponseCookie 사용)
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(cookieProperties.isSecure()) // 로컬 개발 환경(http)에서는 false여야 함. 운영(https)에서는 true
                .sameSite(cookieProperties.getSameSite()) // 또는 "None" (Secure=true 필수)
                .path("/")
                .maxAge(refreshTokenValidity / 1000) // 초 단위
                .build();
        
        response.addHeader("Set-Cookie", refreshCookie.toString());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);

        log.info("======> accessToken= {}", accessToken);

        return ResponseEntity.ok(ApiResult.success(tokens));
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다.")
    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResult<Map<String, String>>> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(401).body(ApiResult.fail(ApiResultCode.INVALID_TOKEN_2));
        }

        // DB에서 토큰 조회 및 검증
        Optional<RefreshTokenDto> tokenDto = refreshTokenService.findByToken(refreshToken);
        if (tokenDto.isEmpty() || tokenDto.get().isRevoked()) {
            return ResponseEntity.status(401).body(ApiResult.fail(ApiResultCode.INVALID_TOKEN_3));
        }

        try {
            refreshTokenService.verifyExpiration(tokenDto.get());
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(ApiResult.fail(ApiResultCode.INVALID_TOKEN_4));
        }

        // JWT 자체 유효성 검증 (선택 사항, DB 만료일과 이중 체크)
        if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body(ApiResult.fail(ApiResultCode.INVALID_TOKEN_5));
        }

        String loginId = jwtUtil.getLoginIdFromToken(refreshToken);

        UserDto user = userService.getUserInfoByLoginId(loginId);

        if (authService.isUserActiveValid(user)) {
            return ResponseEntity.status(401).body(ApiResult.fail(ApiResultCode.INVALID_USER));
        }
        String newAccessToken = jwtUtil.generateAccessToken(loginId, "ROLE_USER");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);

        log.info("======> accessToken= {}", newAccessToken);

        return ResponseEntity.ok(ApiResult.success(tokens));
    }

    @Operation(summary = "로그아웃", description = "리프레시 토큰을 무효화하고 쿠키를 삭제합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResult<String>> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            refreshTokenService.revokeToken(refreshToken);
        }

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieProperties.isSecure()) // 로컬 개발 환경(http)에서는 false여야 함. 운영(https)에서는 true
                .sameSite(cookieProperties.getSameSite()) // 또는 "None" (Secure=true 필수)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok(ApiResult.success(null));
    }
}