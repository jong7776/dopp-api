package com.dopp.doppapi.mapper.user;

import com.dopp.doppapi.dto.user.UserDto;
import com.dopp.doppapi.dto.user.UserPasswordUpdateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    UserDto selectUserInfoByLoginId(@Param("loginId") String loginId);

    List<UserDto> selectUserList(UserDto request);

    void insertUser(UserDto userDto);
    void updateUser(UserDto userDto);
    void deleteUser(@Param("userId") Long userId);
    void updatePassword(UserPasswordUpdateRequest request);
}
