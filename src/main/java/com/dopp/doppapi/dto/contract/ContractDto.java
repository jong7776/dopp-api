package com.dopp.doppapi.dto.contract;

import com.dopp.doppapi.dto.common.MonthlyAmountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "계약 정보 DTO")
public class ContractDto implements MonthlyAmountDto {
    @Schema(description = "계약 고유 ID (PK)")
    private Long contractId;
    
    @Schema(description = "계약 구분 (S: 매출, P: 매입)", example = "S")
    private String type;
    
    @Schema(description = "적용 연도", example = "2023")
    private Integer year;
    
    @Schema(description = "계약명", example = "유지보수 계약")
    private String contractName;
    
    @Schema(description = "업체명", example = "(주)다올")
    private String companyName;
    
    @Schema(description = "계약 시작일", example = "2023-01-01")
    private LocalDate contractStart;
    
    @Schema(description = "계약 종료일", example = "2023-12-31")
    private LocalDate contractEnd;
    
    @Schema(description = "계산서 발행 기준 (선금/잔금, 매월 말일 등)", example = "매월 말일")
    private String invoiceRule;
    
    @Schema(description = "지급 조건 (예: 30일 이내)", example = "익월 10일")
    private String paymentTerm;
    
    @Schema(description = "계약 총액")
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
    
    @Schema(description = "데이터 생성 일시")
    private LocalDateTime createdAt;
    
    @Schema(description = "데이터 수정 일시")
    private LocalDateTime updatedAt;
    
    @Schema(description = "생성자 LOGIN ID")
    private String createdBy;
    
    @Schema(description = "수정자 LOGIN ID")
    private String updatedBy;
}
