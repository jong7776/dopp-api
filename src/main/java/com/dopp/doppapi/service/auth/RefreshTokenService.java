package com.dopp.doppapi.service.auth;

import com.dopp.doppapi.dto.auth.RefreshTokenDto;
import com.dopp.doppapi.mapper.auth.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenMapper refreshTokenMapper;

    @Transactional
    public void createRefreshToken(Long userId, String token, String userAgent, String ipAddress) {
        // 기존 토큰 만료 처리 (선택 사항: 한 유저당 하나의 기기만 허용하려면 주석 해제)
        // refreshTokenMapper.revokeAllUserTokens(userId);

        RefreshTokenDto refreshTokenDto = RefreshTokenDto.builder()
                .userId(userId)
                .token(token)
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .expiresAt(LocalDateTime.now().plusDays(1)) // 1일 후 만료
                .revoked(false)
                .build();

        refreshTokenMapper.insertRefreshToken(refreshTokenDto);
    }

    @Transactional(readOnly = true)
    public Optional<RefreshTokenDto> findByToken(String token) {
        return refreshTokenMapper.findByToken(token);
    }

    @Transactional
    public void verifyExpiration(RefreshTokenDto token) {
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenMapper.revokeToken(token.getToken());
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
    }

    @Transactional
    public void revokeToken(String token) {
        refreshTokenMapper.revokeToken(token);
    }

    @Transactional
    public void revokeAllUserTokens(Long userId) {
        refreshTokenMapper.revokeAllUserTokens(userId);
    }
}
