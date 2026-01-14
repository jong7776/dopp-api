package com.dopp.doppapi.controller.common;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.response.ApiResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {

    protected String getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        return authentication.getName();
    }

    protected ResponseEntity<ApiResult<Void>> checkAuthentication() {
        if (getLoginId() == null) {
            return ResponseEntity.ok(ApiResult.fail(ApiResultCode.UNAUTHORIZED));
        }
        return null;
    }
}
