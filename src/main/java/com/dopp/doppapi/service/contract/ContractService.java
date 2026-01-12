package com.dopp.doppapi.service.contract;

import com.dopp.doppapi.dto.contract.ContractDto;
import com.dopp.doppapi.dto.contract.ContractListRequest;
import com.dopp.doppapi.dto.contract.ContractListResponse;
import com.dopp.doppapi.mapper.contract.ContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractMapper contractMapper;

    public ContractListResponse getContractList(ContractListRequest request) {
        List<ContractDto> list = contractMapper.selectContractList(request);

        Map<String, List<ContractDto>> groupedByType = list.stream()
                .collect(Collectors.groupingBy(ContractDto::getType));

        List<ContractDto> sales = groupedByType.getOrDefault("S", new ArrayList<>());
        List<ContractDto> purchase = groupedByType.getOrDefault("P", new ArrayList<>());

        return new ContractListResponse(sales, purchase);
    }
}
