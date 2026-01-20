package com.dopp.doppapi.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "경비 목록 조회 요청 DTO")
public class ExpenseListRequest {
    @Schema(description = "조회 연도", example = "2023")
    private Integer year;
}
