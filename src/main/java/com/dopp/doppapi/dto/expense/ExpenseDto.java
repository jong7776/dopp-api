package com.dopp.doppapi.dto.expense;

import com.dopp.doppapi.dto.common.MonthlyAmountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "경비 정보 DTO")
public class ExpenseDto implements MonthlyAmountDto {
    @Schema(description = "경비 ID (PK)")
    private Long expenseId;
    
    @Schema(description = "경비 항목명", example = "사무실 임대료")
    private String expenseName;
    
    @Schema(description = "연도", example = "2023")
    private Integer year;
    
    @Schema(description = "총액")
    private Long totalAmount;
    
    @Schema(description = "1월 금액")
    private Long m01;
    @Schema(description = "2월 금액")
    private Long m02;
    @Schema(description = "3월 금액")
    private Long m03;
    @Schema(description = "4월 금액")
    private Long m04;
    @Schema(description = "5월 금액")
    private Long m05;
    @Schema(description = "6월 금액")
    private Long m06;
    @Schema(description = "7월 금액")
    private Long m07;
    @Schema(description = "8월 금액")
    private Long m08;
    @Schema(description = "9월 금액")
    private Long m09;
    @Schema(description = "10월 금액")
    private Long m10;
    @Schema(description = "11월 금액")
    private Long m11;
    @Schema(description = "12월 금액")
    private Long m12;
    
    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정 일시")
    private LocalDateTime updatedAt;
    
    @Schema(description = "생성자")
    private String createdBy;
    
    @Schema(description = "수정자")
    private String updatedBy;
}
