package com.dopp.doppapi.controller.contract;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.dto.contract.ContractListRequest;
import com.dopp.doppapi.dto.contract.ContractListResponse;
import com.dopp.doppapi.service.contract.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/list")
    public ResponseEntity<ApiResult<ContractListResponse>> getContractList(ContractListRequest request) {
        return ResponseEntity.ok(ApiResult.success(contractService.getContractList(request)));
    }
}
