package com.dopp.doppapi.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String loginId;
    private String nickname;
    private String role;
    private Boolean isActive;
    @JsonIgnore
    private String password; // 내부 검증용, 절대 프론트로 반환하지 말 것
}
