package com.dopp.doppapi.controller.expense;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.utils.ExcelUtil;
import com.dopp.doppapi.dto.expense.ExpenseDto;
import com.dopp.doppapi.dto.expense.ExpenseListRequest;
import com.dopp.doppapi.service.expense.ExpenseService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/list")
    public ResponseEntity<ApiResult<List<ExpenseDto>>> getExpenseList(ExpenseListRequest request) {
        return ResponseEntity.ok(ApiResult.success(expenseService.getExpenseList(request)));
    }

    @PostMapping("/list/excel/download")
    public void downloadExcelExpenseList(ExpenseListRequest request, HttpServletResponse response) throws IOException {
        List<ExpenseDto> list = expenseService.downloadExcelExpenseList(request);
        Map<String, String> headerMap = new LinkedHashMap<>();
        headerMap.put("expenseName", "경비 내역");
        headerMap.put("year", "연도");
        headerMap.put("totalAmount", "총액");
        headerMap.put("m01", "1월");
        headerMap.put("m02", "2월");
        headerMap.put("m03", "3월");
        headerMap.put("m04", "4월");
        headerMap.put("m05", "5월");
        headerMap.put("m06", "6월");
        headerMap.put("m07", "7월");
        headerMap.put("m08", "8월");
        headerMap.put("m09", "9월");
        headerMap.put("m10", "10월");
        headerMap.put("m11", "11월");
        headerMap.put("m12", "12월");
        ExcelUtil.downloadExcel(response, list, headerMap, "경비_목록_" + request.getYear(), "경비_목록_" + request.getYear());
        ResponseEntity.ok(ApiResult.success(expenseService.getExpenseList(request)));
    }
}
