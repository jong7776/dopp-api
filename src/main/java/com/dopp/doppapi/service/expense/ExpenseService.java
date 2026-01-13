package com.dopp.doppapi.service.expense;

import com.dopp.doppapi.common.utils.CalculationUtil;
import com.dopp.doppapi.dto.expense.ExpenseDto;
import com.dopp.doppapi.dto.expense.ExpenseListRequest;
import com.dopp.doppapi.mapper.expense.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseMapper expenseMapper;

    public List<ExpenseDto> getExpenseList(ExpenseListRequest request) {
        return expenseMapper.selectExpenseList(request);
    }

    public List<ExpenseDto> downloadExcelExpenseList(ExpenseListRequest request) {
        List<ExpenseDto> list = getExpenseList(request);
        // 합계 계산 및 추가
        CalculationUtil.addTotalRow(list, ExpenseDto::new, dto -> dto.setExpenseName("합계"));

        return list;
    }
}
