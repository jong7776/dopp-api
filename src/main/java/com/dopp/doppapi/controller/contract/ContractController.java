package com.dopp.doppapi.controller.contract;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.utils.ExcelUtil;
import com.dopp.doppapi.controller.common.BaseController;
import com.dopp.doppapi.dto.contract.ContractDeleteRequest;
import com.dopp.doppapi.dto.contract.ContractDto;
import com.dopp.doppapi.dto.contract.ContractListRequest;
import com.dopp.doppapi.dto.contract.ContractListResponse;
import com.dopp.doppapi.service.common.ExcelHeaderMapService;
import com.dopp.doppapi.service.contract.ContractService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/financial-management/contract")
@RequiredArgsConstructor
@Tag(name = "Contract", description = "계약 관리 API")
public class ContractController extends BaseController {
    private final ExcelHeaderMapService excelHeaderMapService;
    private final ContractService contractService;
    private final ObjectMapper objectMapper;

    @PostMapping("/list")
    @Operation(summary = "계약 목록 조회", description = "조건에 맞는 계약 목록을 조회합니다.")
    public ResponseEntity<ApiResult<ContractListResponse>> getContractList(ContractListRequest request) {
        return ResponseEntity.ok(ApiResult.success(contractService.getContractList(request)));
    }

    @PostMapping("/list/excel/download")
    @Operation(summary = "계약 목록 엑셀 다운로드", description = "계약 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadExcelContractList(ContractListRequest request, HttpServletResponse response) throws IOException {
        ContractListResponse contractList = contractService.downloadExcelContractList(request);
        Map<String, String> headerMap = excelHeaderMapService.getContractListHeaderMap(true);

        List<ExcelUtil.ExcelTableData<?>> tableDataList = new ArrayList<>();
        tableDataList.add(new ExcelUtil.ExcelTableData<>(contractList.getSales(), headerMap, "매출 계약 목록"));
        tableDataList.add(new ExcelUtil.ExcelTableData<>(contractList.getPurchase(), headerMap, "매입 계약 목록"));

        ExcelUtil.downloadMultiTableExcel(response, tableDataList, "계약_목록_" + request.getYear(), "계약 현황_" + request.getYear());
    }

    @PostMapping(value = "/list/excel/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "계약 목록 엑셀 업로드", description = "엑셀 파일을 업로드하여 계약 목록을 등록합니다.")
    public ResponseEntity<ApiResult<Void>> uploadExcelContractList(
        @Parameter(description = "엑셀 파일", required = true)
        @RequestPart("file") MultipartFile file
    ) throws IOException {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        
        contractService.uploadExcelContractList(file, getLoginId());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/create")
    @Operation(summary = "계약 등록", description = "새로운 계약을 등록합니다.")
    public ResponseEntity<ApiResult<Void>> createContract(@RequestBody ContractDto request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        contractService.createContract(request, getLoginId());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/update")
    @Operation(summary = "계약 수정", description = "기존 계약 정보를 수정합니다.")
    public ResponseEntity<ApiResult<Void>> updateContract(@RequestBody ContractDto request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        contractService.updateContract(request, getLoginId());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/delete")
    @Operation(summary = "계약 삭제", description = "선택한 계약들을 삭제합니다.")
    public ResponseEntity<ApiResult<Void>> deleteContractList(@RequestBody ContractDeleteRequest request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        contractService.deleteContractList(request);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/delete/all")
    @Operation(summary = "연도별 계약 전체 삭제", description = "특정 연도의 모든 계약을 삭제합니다.")
    public ResponseEntity<ApiResult<Void>> deleteContractByYear(@RequestBody ContractDto request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        contractService.deleteContractByYear(request);
        return ResponseEntity.ok(ApiResult.success(null));
    }
}
