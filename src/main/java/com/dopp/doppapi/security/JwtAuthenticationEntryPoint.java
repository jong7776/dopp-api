package com.dopp.doppapi.security;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.response.ApiResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("======> Unauthorized error: {}", authException.getMessage());

        Object exception = request.getAttribute("exception");
        ApiResultCode resultCode = ApiResultCode.INVALID_TOKEN;

        if (exception instanceof ApiResultCode) {
            resultCode = (ApiResultCode) exception;
        }

        sendErrorResponse(response, resultCode);
    }

    private void sendErrorResponse(HttpServletResponse response, ApiResultCode resultCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResult<Void> apiResult = ApiResult.fail(resultCode);
        String jsonResponse = objectMapper.writeValueAsString(apiResult);

        response.getWriter().write(jsonResponse);
    }
}
