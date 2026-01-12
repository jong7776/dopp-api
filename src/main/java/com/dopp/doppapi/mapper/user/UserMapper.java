package com.dopp.doppapi.mapper.user;

import com.dopp.doppapi.dto.user.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    UserDto selectUserInfoByLoginId(@Param("loginId") String loginId);

    List<UserDto> selectUserList(UserDto request);
}