package com.dopp.doppapi.controller.user;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.dto.user.UserDto;
import com.dopp.doppapi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
