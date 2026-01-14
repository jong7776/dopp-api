package com.dopp.doppapi.mapper.expense;

import com.dopp.doppapi.dto.expense.ExpenseDto;
import com.dopp.doppapi.dto.expense.ExpenseListRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExpenseMapper {
    List<ExpenseDto> selectExpenseList(ExpenseListRequest request);

    void insertExpenseList(@Param("list") List<ExpenseDto> list, @Param("loginId") String loginId);
}
