package com.dopp.doppapi.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "API Result", description = "API 요청 결과")
public class ApiResult<T> {

    @Schema(name = "API 요청 결과 코드")
    private String code;

    @Schema(name = "API 요청 결과 메시지")
    private String message;

    @Schema(name = "API 요청 결과 화면메시지")
    private String frontMessage;

    @Schema(name = "API 요청 결과 데이터")
    private T data;

    public static <T> ApiResult<T> success(T data) {
        return ApiResult.<T>builder()
                .code(ApiResultCode.SUCCESS.getCode())
                .message(ApiResultCode.SUCCESS.getMessage())
                .frontMessage(ApiResultCode.SUCCESS.getFrontMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResult<T> fail(ApiResultCode resultCode) {
        return ApiResult.<T>builder()
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .frontMessage(resultCode.getFrontMessage())
                .data(null)
                .build();
    }
}