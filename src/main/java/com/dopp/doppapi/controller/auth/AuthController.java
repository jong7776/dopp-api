package com.dopp.doppapi.controller.auth;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.response.ApiResultCode;
import com.dopp.doppapi.dto.user.LoginRequest;
import com.dopp.doppapi.dto.user.UserDto;
import com.dopp.doppapi.security.JwtUtil;
import com.dopp.doppapi.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResult<Map<String, String>>> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        UserDto user = userService.login(request);

        String accessToken = jwtUtil.generateAccessToken(user.getLoginId(), "ROLE_USER");
        String refreshToken = jwtUtil.generateRefreshToken(user.getLoginId());

        // Refresh Token을 HttpOnly Cookie에 저장
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); // HTTPS 사용 시 true
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(1 * 24 * 60 * 60); // 1일
        response.addCookie(refreshCookie);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);

        return ResponseEntity.ok(ApiResult.success(tokens));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResult<Map<String, String>>> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body(ApiResult.fail(ApiResultCode.UNAUTHORIZED));
        }

        String loginId = jwtUtil.getLoginIdFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(loginId, "ROLE_USER");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);

        return ResponseEntity.ok(ApiResult.success(tokens));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResult<String>> logout(HttpServletResponse response) {
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0); // 쿠키 삭제
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(ApiResult.success("Logged out successfully"));
    }
}
