package com.dopp.doppapi.common.response;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
public class ApiResponseUtil {
    public static <T> ResponseEntity<ApiResult<T>> ok(T data) {
        return ResponseEntity.ok(ApiResult.success(data));
    }

    public static ResponseEntity<ApiResult<Void>> fail(ApiResultCode code, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(ApiResult.fail(code));
    }
}
