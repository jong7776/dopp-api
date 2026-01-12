package com.dopp.doppapi.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApiResultCode {
    SUCCESS("000000", "성공", "성공"),
    FAIL("999999", "서버 오류", "서버 오류"),
    INVALID_PARAM("000400", "잘못된 요청", "잘못된 요청입니다."),

    INVALID_TOKEN("A00001", "access token 만료", "인증 실패하였습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_1("A00002", "refresh token이 없습니다.", "인증 실패하였습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_2("A00003", "db 조회 null || isRevoked = true", "인증 실패하였습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_3("A00004", "토크 유효시간 만료 1", "인증 실패하였습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_4("A00005", "토크 유효시간 만료 2", "인증 실패하였습니다. 다시 로그인해주세요."),
    INVALID_PW("A00006", "비밀번호 오류", "잘못된 비밀번호입니다. 다시 로그인해주세요."),

    FAIL_DOWNLOAD_EXCEL("F00001", "엑셀 다운로드 실패", "엑셀 다운로드에 실패하였습니다.");

    private final String code;
    private final String message;
    private final String frontMessage;
}
