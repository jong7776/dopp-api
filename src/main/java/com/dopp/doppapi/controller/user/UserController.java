package com.dopp.doppapi.controller.user;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.dto.auth.UserDto;
import com.dopp.doppapi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<ApiResult<UserDto>> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserDto user = userService.getUserByLoginId(userDetails.getUsername());
        return ResponseEntity.ok(ApiResult.success(user));
    }
}
