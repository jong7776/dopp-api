package com.dopp.doppapi.controller.expense;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.dto.expense.ExpenseDto;
import com.dopp.doppapi.dto.expense.ExpenseListRequest;
import com.dopp.doppapi.service.expense.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/list")
    public ResponseEntity<ApiResult<List<ExpenseDto>>> getExpenseList(ExpenseListRequest request) {
        return ResponseEntity.ok(ApiResult.success(expenseService.getExpenseList(request)));
    }
}
