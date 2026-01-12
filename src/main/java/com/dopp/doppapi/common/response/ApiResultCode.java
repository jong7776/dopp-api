package com.dopp.doppapi.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApiResultCode {
    SUCCESS("0000", "성공", "성공"),
    FAIL("9999", "서버 오류", "서버 오류"),

    INVALID_PARAM("0400", "잘못된 요청", "잘못된 요청입니다."),
    INVALID_TOKEN("0401", "access token 만료", "인증 실패하였습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_1("0401-1", "refresh token이 없습니다.", "인증 실패하였습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_2("0401-2", "db 조회 null || isRevoked = true", "인증 실패하였습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_3("0401-3", "토크 유효시간 만료 1", "인증 실패하였습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_4("0401-4", "토크 유효시간 만료 2", "인증 실패하였습니다. 다시 로그인해주세요."),
    INVALID_PW("0402", "비밀번호 오류", "잘못된 비밀번호입니다. 다시 로그인해주세요."),

    NOT_FOUND("0404", "데이터 없음", "데이터 없음");

    private final String code;
    private final String message;
    private final String frontMessage;
}
