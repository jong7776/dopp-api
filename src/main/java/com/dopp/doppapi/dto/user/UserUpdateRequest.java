package com.dopp.doppapi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "사용자 수정 요청 DTO")
public class UserUpdateRequest {
    @Schema(description = "사용자 ID (PK)", example = "1")
    private Long userId;
    
    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;
    
    @Schema(description = "권한", example = "user")
    private String role;
    
    @Schema(description = "활성 여부", example = "true")
    private Boolean isActive;
}
