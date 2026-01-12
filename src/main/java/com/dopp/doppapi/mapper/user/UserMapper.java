package com.dopp.doppapi.mapper.user;

import com.dopp.doppapi.dto.user.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    UserDto selectUserByLoginId(@Param("loginId") String loginId);
}