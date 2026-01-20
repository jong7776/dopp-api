package com.dopp.doppapi.controller.expense;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.utils.ExcelUtil;
import com.dopp.doppapi.controller.common.BaseController;
import com.dopp.doppapi.dto.expense.ExpenseDeleteRequest;
import com.dopp.doppapi.dto.expense.ExpenseDto;
import com.dopp.doppapi.dto.expense.ExpenseListRequest;
import com.dopp.doppapi.service.common.ExcelHeaderMapService;
import com.dopp.doppapi.service.expense.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/financial-management/expense")
@RequiredArgsConstructor
@Tag(name = "Expense", description = "경비 관리 API")
public class ExpenseController extends BaseController {
    private final ExcelHeaderMapService excelHeaderMapService;
    private final ExpenseService expenseService;

    @PostMapping("/list")
    @Operation(summary = "경비 목록 조회", description = "조건에 맞는 경비 목록을 조회합니다.")
    public ResponseEntity<ApiResult<List<ExpenseDto>>> getExpenseList(ExpenseListRequest request) {
        return ResponseEntity.ok(ApiResult.success(expenseService.getExpenseList(request)));
    }

    @PostMapping("/list/excel/download")
    @Operation(summary = "경비 목록 엑셀 다운로드", description = "경비 목록을 엑셀 파일로 다운로드합니다.")
    public void downloadExcelExpenseList(ExpenseListRequest request, HttpServletResponse response) throws IOException {
        List<ExpenseDto> list = expenseService.downloadExcelExpenseList(request);
        Map<String, String> headerMap = excelHeaderMapService.getExpenseListHeaderMap(true);

        ExcelUtil.downloadExcel(response, list, headerMap, "경비_목록_" + request.getYear(), "경비_목록_" + request.getYear());
        ResponseEntity.ok(ApiResult.success(expenseService.getExpenseList(request)));
    }

    @PostMapping(value = "/list/excel/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "경비 목록 엑셀 업로드", description = "엑셀 파일을 업로드하여 경비 목록을 등록합니다.")
    public ResponseEntity<ApiResult<Void>> uploadExcelContractList(
            @Parameter(description = "엑셀 파일", required = true)
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }

        expenseService.uploadExcelExpenseList(file, getLoginId());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/create")
    @Operation(summary = "경비 등록", description = "새로운 경비를 등록합니다.")
    public ResponseEntity<ApiResult<Void>> createExpense(@RequestBody ExpenseDto request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        expenseService.createExpense(request, getLoginId());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/update")
    @Operation(summary = "경비 수정", description = "기존 경비 정보를 수정합니다.")
    public ResponseEntity<ApiResult<Void>> updateExpense(@RequestBody ExpenseDto request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        expenseService.updateExpense(request, getLoginId());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/delete")
    @Operation(summary = "경비 삭제", description = "선택한 경비들을 삭제합니다.")
    public ResponseEntity<ApiResult<Void>> deleteExpenseList(@RequestBody ExpenseDeleteRequest request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        expenseService.deleteExpenseList(request.getExpenseIds());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/delete/all")
    @Operation(summary = "연도별 경비 전체 삭제", description = "특정 연도의 모든 경비를 삭제합니다.")
    public ResponseEntity<ApiResult<Void>> deleteAllExpenseList(@RequestBody ExpenseDto request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        expenseService.deleteAllExpenseList(request);
        return ResponseEntity.ok(ApiResult.success(null));
    }
}
