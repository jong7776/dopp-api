package com.dopp.doppapi.dto.contract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "계약 목록 응답 DTO")
public class ContractListResponse {
    @Schema(description = "매출 계약 목록")
    List<ContractDto> sales; 
    
    @Schema(description = "매입 계약 목록")
    List<ContractDto> purchase;
}
