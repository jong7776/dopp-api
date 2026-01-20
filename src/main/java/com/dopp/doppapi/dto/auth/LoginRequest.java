package com.dopp.doppapi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class LoginRequest {
    @Schema(description = "로그인 ID", example = "dopp")
    private String loginId;

    @Schema(description = "비밀번호", example = "1234")
    private String password;
}
