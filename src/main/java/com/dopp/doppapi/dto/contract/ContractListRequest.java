package com.dopp.doppapi.dto.contract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "계약 목록 조회 요청 DTO")
public class ContractListRequest {
    @Schema(description = "계약 구분 (S: 매출, P: 매입)", example = "S")
    private String type;
    
    @Schema(description = "조회 연도", example = "2023")
    private Integer year;
}
