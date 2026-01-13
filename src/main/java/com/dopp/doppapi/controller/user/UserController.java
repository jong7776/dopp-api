package com.dopp.doppapi.controller.user;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.utils.ExcelUtil;
import com.dopp.doppapi.dto.user.UserDto;
import com.dopp.doppapi.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<ApiResult<UserDto>> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResult.success(userService.getUserInfo(userDetails.getUsername())));
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResult<List<UserDto>>> getUserList(UserDto request) {
        return ResponseEntity.ok(ApiResult.success(userService.getUserList(request)));
    }

    @PostMapping("/list/excel/download")
    public void downloadUserListExcel(UserDto request, HttpServletResponse response) throws IOException {
        List<UserDto> list = userService.getUserList(request);

        Map<String, String> headerMap = new LinkedHashMap<>();
        headerMap.put("loginId", "로그인 ID");
        headerMap.put("nickname", "닉네임");
        headerMap.put("role", "권한");
        headerMap.put("isActive", "활성 여부");
        ExcelUtil.downloadExcel(response, list, headerMap, "사용자_목록", "사용자 목록");
    }
}
