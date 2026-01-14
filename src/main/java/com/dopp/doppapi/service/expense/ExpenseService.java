package com.dopp.doppapi.service.expense;

import com.dopp.doppapi.common.utils.CalculationUtil;
import com.dopp.doppapi.common.utils.ExcelUtil;
import com.dopp.doppapi.dto.expense.ExpenseDto;
import com.dopp.doppapi.dto.expense.ExpenseListRequest;
import com.dopp.doppapi.mapper.expense.ExpenseMapper;
import com.dopp.doppapi.service.common.ExcelHeaderMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExcelHeaderMapService excelHeaderMapService;
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

    public void uploadExcelExpenseList(MultipartFile file, String loginId) throws IOException {
        Map<String, String> headerMap = excelHeaderMapService.getExpenseListHeaderMap(false);

        // 컬럼 순서대로 매핑하는 메서드 사용
        List<ExpenseDto> list = ExcelUtil.uploadExcelByColumnOrder(file, ExpenseDto.class, headerMap);

        if (!list.isEmpty()) {
            expenseMapper.insertExpenseList(list, loginId);
        }
    }
}
