package com.dopp.doppapi.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 정보 DTO")
public class UserDto {
    @Schema(description = "사용자 ID (PK)")
    private Long userId;
    
    @Schema(description = "로그인 ID")
    private String loginId;
    
    @Schema(description = "닉네임")
    private String nickname;
    
    @Schema(description = "권한 (admin, staff 등)")
    private String role;
    
    @Schema(description = "활성 여부")
    private Boolean isActive;
    
    @Schema(description = "최초 로그인 여부")
    private Boolean isFirstLogin;
    
    @JsonIgnore
    @Schema(description = "비밀번호 (내부용, 반환 안됨)", hidden = true)
    private String password; 
    
    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정 일시")
    private LocalDateTime updatedAt;
    
    @Schema(description = "생성자")
    private String createdBy;
    
    @Schema(description = "수정자")
    private String updatedBy;
}
