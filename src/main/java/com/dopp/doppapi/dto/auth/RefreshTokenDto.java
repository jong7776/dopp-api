package com.dopp.doppapi.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {
    private Long id;
    private Long userId;
    private String token;
    private String userAgent;
    private String ipAddress;
    private LocalDateTime expiresAt;
    private boolean revoked;
    private LocalDateTime createdAt;
}
