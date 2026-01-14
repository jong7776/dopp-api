package com.dopp.doppapi.service.contract;

import com.dopp.doppapi.common.utils.CalculationUtil;
import com.dopp.doppapi.common.utils.ExcelUtil;
import com.dopp.doppapi.dto.contract.ContractDto;
import com.dopp.doppapi.dto.contract.ContractListRequest;
import com.dopp.doppapi.dto.contract.ContractListResponse;
import com.dopp.doppapi.mapper.contract.ContractMapper;
import com.dopp.doppapi.service.common.ExcelHeaderMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ExcelHeaderMapService excelHeaderMapService;
    private final ContractMapper contractMapper;

    public ContractListResponse getContractList(ContractListRequest request) {
        List<ContractDto> list = contractMapper.selectContractList(request);

        Map<String, List<ContractDto>> groupedByType = list.stream()
                .collect(Collectors.groupingBy(ContractDto::getType));

        List<ContractDto> sales = groupedByType.getOrDefault("S", new ArrayList<>());
        List<ContractDto> purchase = groupedByType.getOrDefault("P", new ArrayList<>());

        return new ContractListResponse(sales, purchase);
    }

    public ContractListResponse downloadExcelContractList(ContractListRequest request) {
        ContractListResponse list = getContractList(request);
        // 합계 계산 및 추가
        CalculationUtil.addTotalRow(list.getSales(), ContractDto::new, dto -> dto.setContractName("합계"));
        CalculationUtil.addTotalRow(list.getPurchase(), ContractDto::new, dto -> dto.setContractName("합계"));

        return list;
    }

    @Transactional
    public void uploadExcelContractList(MultipartFile file, String loginId) throws IOException {
        Map<String, String> headerMap = excelHeaderMapService.getContractListHeaderMap(false);

        // 컬럼 순서대로 매핑하는 메서드 사용
        List<ContractDto> list = ExcelUtil.uploadExcelByColumnOrder(file, ContractDto.class, headerMap);

        if (!list.isEmpty()) {
            contractMapper.insertContractList(list, loginId);
        }
    }
}
