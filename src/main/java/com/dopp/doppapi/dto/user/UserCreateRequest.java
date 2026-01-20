package com.dopp.doppapi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "사용자 생성 요청 DTO")
public class UserCreateRequest {
    @Schema(description = "로그인 ID", example = "user123")
    private String loginId;
    
    @Schema(description = "비밀번호", example = "password123!")
    private String password;
    
    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;
    
    @Schema(description = "권한", example = "staff")
    private String role;
}
