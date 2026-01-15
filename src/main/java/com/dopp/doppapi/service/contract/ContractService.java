package com.dopp.doppapi.service.contract;

import com.dopp.doppapi.common.utils.CalculationUtil;
import com.dopp.doppapi.common.utils.ExcelUtil;
import com.dopp.doppapi.dto.contract.ContractDeleteRequest;
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
import java.util.function.Function;
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
            list.forEach(this::convertPurchaseAmountToNegative);
            contractMapper.insertContractList(list, loginId);
        }
    }

    @Transactional
    public void createContract(ContractDto contractDto, String loginId) {
        contractDto.setCreatedBy(loginId);
        contractDto.setUpdatedBy(loginId);
        convertPurchaseAmountToNegative(contractDto);
        contractMapper.insertContract(contractDto);
    }

    @Transactional
    public void updateContract(ContractDto contractDto, String loginId) {
        contractDto.setUpdatedBy(loginId);
        convertPurchaseAmountToNegative(contractDto);
        contractMapper.updateContract(contractDto);
    }

    @Transactional
    public void deleteContractList(ContractDeleteRequest request) {
        if (request.getContractIds() != null && !request.getContractIds().isEmpty()) {
            contractMapper.deleteContractList(request.getContractIds());
        }
    }

    @Transactional
    public void deleteContractByYear(ContractDto request) {
        contractMapper.deleteContractByYear(request);
    }

    private void convertPurchaseAmountToNegative(ContractDto dto) {
        if ("P".equals(dto.getType())) {
            Function<Long, Long> toNegative = val -> (val != null && val > 0) ? Long.valueOf(-val) : val;

            dto.setM01(toNegative.apply(dto.getM01()));
            dto.setM02(toNegative.apply(dto.getM02()));
            dto.setM03(toNegative.apply(dto.getM03()));
            dto.setM04(toNegative.apply(dto.getM04()));
            dto.setM05(toNegative.apply(dto.getM05()));
            dto.setM06(toNegative.apply(dto.getM06()));
            dto.setM07(toNegative.apply(dto.getM07()));
            dto.setM08(toNegative.apply(dto.getM08()));
            dto.setM09(toNegative.apply(dto.getM09()));
            dto.setM10(toNegative.apply(dto.getM10()));
            dto.setM11(toNegative.apply(dto.getM11()));
            dto.setM12(toNegative.apply(dto.getM12()));
        }
    }
}
