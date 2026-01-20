package com.dopp.doppapi.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "경비 삭제 요청 DTO")
public class ExpenseDeleteRequest {
    @Schema(description = "삭제할 경비 ID 목록")
    private List<Long> expenseIds;
}
