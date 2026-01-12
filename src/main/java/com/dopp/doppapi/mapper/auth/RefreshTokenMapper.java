package com.dopp.doppapi.mapper.auth;

import com.dopp.doppapi.dto.auth.RefreshTokenDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {

    void insertRefreshToken(RefreshTokenDto refreshTokenDto);

    Optional<RefreshTokenDto> findByToken(@Param("token") String token);

    void revokeAllUserTokens(@Param("userId") Long userId);

    void revokeToken(@Param("token") String token);
}
