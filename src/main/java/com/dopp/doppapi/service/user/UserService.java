package com.dopp.doppapi.service.user;

import com.dopp.doppapi.dto.user.UserCreateRequest;
import com.dopp.doppapi.dto.user.UserDto;
import com.dopp.doppapi.dto.user.UserPasswordUpdateRequest;
import com.dopp.doppapi.dto.user.UserUpdateRequest;
import com.dopp.doppapi.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public UserDto getUserInfoByLoginId(String loginId) {
        return userMapper.selectUserInfoByLoginId(loginId);
    }
    public UserDto getUserInfoByUserId(Long userId) {
        return userMapper.selectUserInfoByUserId(userId);
    }

    public List<UserDto> getUserList(UserDto request) {
        return userMapper.selectUserList(request);
    }

    @Transactional
    public void createUser(UserCreateRequest request, String loginId) {
        UserDto userDto = new UserDto();
        userDto.setLoginId(request.getLoginId());
        userDto.setPassword(request.getPassword());
        userDto.setNickname(request.getNickname());
        userDto.setRole(request.getRole());
        userDto.setIsActive(true);
        userDto.setIsFirstLogin(true);
        userDto.setCreatedBy(loginId);
        userDto.setUpdatedBy(loginId);

        userMapper.insertUser(userDto);
    }

    @Transactional
    public void updateUser(UserUpdateRequest request, String loginId) {
        UserDto userDto = new UserDto();
        userDto.setUserId(request.getUserId());
        userDto.setNickname(request.getNickname());
        userDto.setRole(request.getRole());
        userDto.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        userDto.setUpdatedBy(loginId);

        userMapper.updateUser(userDto);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userMapper.deleteUser(userId);
    }

    @Transactional
    public void updatePassword(UserPasswordUpdateRequest request) {
        userMapper.updatePassword(request);
    }
}
