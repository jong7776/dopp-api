package com.dopp.doppapi.dto.contract;

import com.dopp.doppapi.dto.common.MonthlyAmountDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto implements MonthlyAmountDto {
    private Long contractId;
    private String type;
    private Integer year;
    private String contractName;
    private String companyName;
    private LocalDate contractStart;
    private LocalDate contractEnd;
    private String invoiceRule;
    private String paymentTerm;
    private Long totalAmount;
    private Long m01;
    private Long m02;
    private Long m03;
    private Long m04;
    private Long m05;
    private Long m06;
    private Long m07;
    private Long m08;
    private Long m09;
    private Long m10;
    private Long m11;
    private Long m12;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
