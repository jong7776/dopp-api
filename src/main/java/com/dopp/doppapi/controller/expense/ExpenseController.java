package com.dopp.doppapi.controller.expense;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.utils.ExcelUtil;
import com.dopp.doppapi.controller.common.BaseController;
import com.dopp.doppapi.dto.expense.ExpenseDeleteRequest;
import com.dopp.doppapi.dto.expense.ExpenseDto;
import com.dopp.doppapi.dto.expense.ExpenseListRequest;
import com.dopp.doppapi.service.common.ExcelHeaderMapService;
import com.dopp.doppapi.service.expense.ExpenseService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController extends BaseController {
    private final ExcelHeaderMapService excelHeaderMapService;
    private final ExpenseService expenseService;

    @PostMapping("/list")
    public ResponseEntity<ApiResult<List<ExpenseDto>>> getExpenseList(ExpenseListRequest request) {
        return ResponseEntity.ok(ApiResult.success(expenseService.getExpenseList(request)));
    }

    @PostMapping("/list/excel/download")
    public void downloadExcelExpenseList(ExpenseListRequest request, HttpServletResponse response) throws IOException {
        List<ExpenseDto> list = expenseService.downloadExcelExpenseList(request);
        Map<String, String> headerMap = excelHeaderMapService.getExpenseListHeaderMap(true);

        ExcelUtil.downloadExcel(response, list, headerMap, "경비_목록_" + request.getYear(), "경비_목록_" + request.getYear());
        ResponseEntity.ok(ApiResult.success(expenseService.getExpenseList(request)));
    }

    @PostMapping(value = "/list/excel/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
    public ResponseEntity<ApiResult<Void>> createExpense(@RequestBody ExpenseDto expenseDto) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        expenseService.createExpense(expenseDto, getLoginId());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResult<Void>> updateExpense(@RequestBody ExpenseDto expenseDto) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        expenseService.updateExpense(expenseDto, getLoginId());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResult<Void>> deleteExpenses(@RequestBody ExpenseDeleteRequest request) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        expenseService.deleteExpenses(request.getExpenseIds());
        return ResponseEntity.ok(ApiResult.success(null));
    }

    @PostMapping("/delete/all")
    public ResponseEntity<ApiResult<Void>> deleteAllExpenses(@RequestBody ExpenseDto expenseDto) {
        ResponseEntity<ApiResult<Void>> authResponse = checkAuthentication();
        if (authResponse != null) {
            return authResponse;
        }
        expenseService.deleteAllExpenses(expenseDto);
        return ResponseEntity.ok(ApiResult.success(null));
    }
}
