package com.dopp.doppapi.service.user;

import com.dopp.doppapi.dto.user.UserDto;
import com.dopp.doppapi.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public UserDto getUserInfo(String loginId) {
        return userMapper.selectUserInfoByLoginId(loginId);
    }

    public List<UserDto> getUserList(UserDto request) {
        return userMapper.selectUserList(request);
    }
}