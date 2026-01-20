package com.dopp.doppapi.controller.user;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.response.ApiResultCode;
import com.dopp.doppapi.common.utils.ExcelUtil;
import com.dopp.doppapi.controller.common.BaseController;
import com.dopp.doppapi.dto.user.UserCreateRequest;
import com.dopp.doppapi.dto.user.UserDto;
import com.dopp.doppapi.dto.user.UserPasswordUpdateRequest;
import com.dopp.doppapi.dto.user.UserUpdateRequest;
import com.dopp.doppapi.security.JwtUtil;
import com.dopp.doppapi.service.auth.AuthService;
import com.dopp.doppapi.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관리 API")
public class UserController extends BaseController {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/info")
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    public ResponseEntity<ApiResult<UserDto>> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResult.success(userService.getUserInfoByLoginId(userDetails.getUsername())));
    }

    @PostMapping("/list")
    @Operation(summary = "사용자 목록 조회", description = "조건에 맞는 사용자 목록을 조회합니다.")
    public ResponseEntity<ApiResult<List<UserDto>>> getUserList(UserDto request) {
        return ResponseEntity.ok(ApiResult.success(userService.getUserList(request)));
    }

    @PostMapping("/list/excel/download")
    @Operation(summary = "사용자 목록 엑셀 다운로드", description = "사용자 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadUserListExcel(UserDto request, HttpServletResponse response) throws IOException {
        List<UserDto> list = userService.getUserList(request);

        Map<String, String> headerMap = new LinkedHashMap<>();
        headerMap.put("loginId", "로그인 ID");
        headerMap.put("nickname", "닉네임");
        headerMap.put("role", "권한");
        headerMap.put("isActive", "활성 여부");
        ExcelUtil.downloadExcel(response, list, headerMap, "사용자_목록", "사용자 목록");
    }

    @PostMapping("/create")
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    public ResponseEntity<ApiResult<Void>> createUser(@RequestBody UserCreateRequest request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        userService.createUser(request, getLoginId());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/update")
    @Operation(summary = "사용자 정보 수정", description = "사용자의 정보(닉네임, 권한, 활성여부)를 수정합니다.")
    public ResponseEntity<ApiResult<Void>> updateUser(@RequestBody UserUpdateRequest request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        userService.updateUser(request, getLoginId());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/delete")
    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    public ResponseEntity<ApiResult<Void>> deleteUser(
            @Parameter(description = "삭제할 사용자 ID", required = true)
            @RequestParam Long userId
    ) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/password/update")
    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.")
    public ResponseEntity<ApiResult<Void>> updatePassword(
            @RequestBody UserPasswordUpdateRequest request
    ) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        String loginId = getLoginId();
        // 토큰 로그인 아이디로 회원 활성 여부 체크
        UserDto user = userService.getUserInfoByLoginId(loginId);
        if (authService.isPasswordValid(user, request.getCurrentPassword())) {
            return ResponseEntity.status(401).body(ApiResult.fail(ApiResultCode.INVALID_PW));
        }

        if (authService.isUserActiveValid(user)) {
            return ResponseEntity.status(401).body(ApiResult.fail(ApiResultCode.INVALID_USER));
        }
        request.setLoginId(loginId);
        request.setUpdatedBy(loginId);
        request.setNewPassword(authService.getPasswordEncode(request.getNewPassword()));
        userService.updatePassword(request);
        return ResponseEntity.ok(ApiResult.success(null));
    }
}
