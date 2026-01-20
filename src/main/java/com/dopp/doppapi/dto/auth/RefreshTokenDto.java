package com.dopp.doppapi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리프레시 토큰 정보 DTO")
public class RefreshTokenDto {
    @Schema(description = "리프레시 토큰 ID")
    private Long id;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "리프레시 토큰 값")
    private String token;

    @Schema(description = "사용자 에이전트 정보")
    private String userAgent;

    @Schema(description = "IP 주소")
    private String ipAddress;

    @Schema(description = "만료 일시")
    private LocalDateTime expiresAt;

    @Schema(description = "토큰 폐기 여부")
    private boolean revoked;

    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;
}
