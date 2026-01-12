package com.dopp.doppapi.controller.sample;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.dto.sample.SampleDto;
import com.dopp.doppapi.service.sample.SampleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SampleController {
    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping("/api/sample")
    @Operation(summary = "sample api", description = "sample api 조회")
    public ResponseEntity<ApiResult<List<SampleDto>>> selectSample() {
        return ResponseEntity.ok(
                ApiResult.success(sampleService.selectSample())
        );
    }
}
