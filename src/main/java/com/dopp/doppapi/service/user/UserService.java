package com.dopp.doppapi.service.user;

import com.dopp.doppapi.dto.user.LoginRequest;
import com.dopp.doppapi.dto.user.UserDto;
import com.dopp.doppapi.mapper.user.UserMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper,@Lazy PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public UserDto login(LoginRequest request) {
        UserDto user = userMapper.selectUserByLoginId(request.getLoginId());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid login");
        }
        return user;
    }

    public UserDto getUserByLoginId(String loginId) {
        return userMapper.selectUserByLoginId(loginId);
    }
}