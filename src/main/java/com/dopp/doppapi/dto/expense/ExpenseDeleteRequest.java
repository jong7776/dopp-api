package com.dopp.doppapi.dto.expense;

import lombok.Data;

import java.util.List;

@Data
public class ExpenseDeleteRequest {
    private List<Long> expenseIds;
}
