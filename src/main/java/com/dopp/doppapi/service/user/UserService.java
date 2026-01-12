package com.dopp.doppapi.service.user;

import com.dopp.doppapi.dto.auth.UserDto;
import com.dopp.doppapi.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public UserDto getUserByLoginId(String loginId) {
        return userMapper.selectUserByLoginId(loginId);
    }
}