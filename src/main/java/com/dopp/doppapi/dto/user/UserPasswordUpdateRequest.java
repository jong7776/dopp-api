package com.dopp.doppapi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "사용자 비밀번호 변경 요청 DTO")
public class UserPasswordUpdateRequest {
    @Schema(description = "로그인 ID", hidden = true)
    private String loginId;

    @Schema(description = "현재 비밀번호 (본인 변경 시 필요)", example = "oldPassword123!")
    private String currentPassword;
    
    @Schema(description = "새 비밀번호", example = "newPassword123!")
    private String newPassword;

    @Schema(description = "최초 로그인 여부", example = "false", hidden = true)
    private Boolean isFirstLogin = false;

    @Schema(description = "수정자", hidden = true)
    private String updatedBy;
}
