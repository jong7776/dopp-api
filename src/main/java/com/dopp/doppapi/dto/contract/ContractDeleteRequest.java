package com.dopp.doppapi.dto.contract;

import lombok.Data;

import java.util.List;

@Data
public class ContractDeleteRequest {
    private List<Long> contractIds;
}
