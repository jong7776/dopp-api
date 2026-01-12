package com.dopp.doppapi.common.exception;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.response.ApiResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResult<Void>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(ApiResult.fail(ApiResultCode.INVALID_PARAM));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.fail(ApiResultCode.FAIL));
    }

    // 파일 다운로드 API 전용 IOException 처리
    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = request.getRequestURI();
        log.error("======> Excel 다운로드 중 IOException 발생: {}", path, e);

        if (path.contains("/excel/download")) {
            // 다운로드 API에서만 JSON 반환
            response.reset();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ApiResult<Void> errorResult = ApiResult.fail(ApiResultCode.FAIL_DOWNLOAD_EXCEL);
            objectMapper.writeValue(response.getWriter(), errorResult);

        } else {
            // 일반 API는 그대로 Exception 처리로 위임
            throw new Exception();
        }
    }
}
