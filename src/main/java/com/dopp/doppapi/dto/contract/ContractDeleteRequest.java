package com.dopp.doppapi.dto.contract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "계약 삭제 요청 DTO")
public class ContractDeleteRequest {
    @Schema(description = "삭제할 계약 ID 목록")
    private List<Long> contractIds;
}
