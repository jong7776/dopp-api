package com.dopp.doppapi.dto.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractListResponse {
    List<ContractDto> sales; // 매출
    List<ContractDto> purchase; // 매입
}
