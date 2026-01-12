package com.dopp.doppapi.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApiResultCode {
    SUCCESS("0000", "성공"),
    FAIL("9999", "서버 오류"),
    INVALID_PARAM("0400", "잘못된 요청"),
    UNAUTHORIZED("0401", "인증 실패"),
    NOT_FOUND("0404", "데이터 없음");

    private final String code;
    private final String message;
}
