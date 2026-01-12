package com.dopp.doppapi.controller.contract;

import com.dopp.doppapi.common.response.ApiResult;
import com.dopp.doppapi.common.utils.ExcelUtil;
import com.dopp.doppapi.dto.contract.ContractDto;
import com.dopp.doppapi.dto.contract.ContractListRequest;
import com.dopp.doppapi.dto.contract.ContractListResponse;
import com.dopp.doppapi.service.contract.ContractService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final ObjectMapper objectMapper;

    @PostMapping("/list")
    public ResponseEntity<ApiResult<ContractListResponse>> getContractList(ContractListRequest request) {
        return ResponseEntity.ok(ApiResult.success(contractService.getContractList(request)));
    }

    @PostMapping("/list/excel/download")
    public void downloadContractListExcel(ContractListRequest request, HttpServletResponse response) throws IOException {
        ContractListResponse contractList = contractService.getContractList(request);

        // 엑셀 헤더 설정
        Map<String, String> headerMap = new LinkedHashMap<>();
//        headerMap.put("contractId", "계약 ID");
//        headerMap.put("type", "구분");
        headerMap.put("year", "연도");
        headerMap.put("contractName", "계약명");
        headerMap.put("companyName", "업체명");
        headerMap.put("contractStart", "계약 시작일");
        headerMap.put("contractEnd", "계약 종료일");
        headerMap.put("totalAmount", "총액");
        headerMap.put("m01", "1월");
        headerMap.put("m02", "2월");
        headerMap.put("m03", "3월");
        headerMap.put("m04", "4월");
        headerMap.put("m05", "5월");
        headerMap.put("m06", "6월");
        headerMap.put("m07", "7월");
        headerMap.put("m08", "8월");
        headerMap.put("m09", "9월");
        headerMap.put("m10", "10월");
        headerMap.put("m11", "11월");
        headerMap.put("m12", "12월");

        // 합계 계산 및 추가
        addTotalRow(contractList.getSales());
        addTotalRow(contractList.getPurchase());

        List<ExcelUtil.ExcelTableData<?>> tableDataList = new ArrayList<>();
        tableDataList.add(new ExcelUtil.ExcelTableData<>(contractList.getSales(), headerMap, "매출 계약 목록"));
        tableDataList.add(new ExcelUtil.ExcelTableData<>(contractList.getPurchase(), headerMap, "매입 계약 목록"));

        ExcelUtil.downloadMultiTableExcel(response, tableDataList, "계약_목록_" + request.getYear(), "계약 현황_" + request.getYear());
    }

    private void addTotalRow(List<ContractDto> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        long totalAmount = 0;
        long[] monthlyTotals = new long[12];

        for (ContractDto dto : list) {
            totalAmount += dto.getTotalAmount() != null ? dto.getTotalAmount() : 0;
            monthlyTotals[0] += dto.getM01() != null ? dto.getM01() : 0;
            monthlyTotals[1] += dto.getM02() != null ? dto.getM02() : 0;
            monthlyTotals[2] += dto.getM03() != null ? dto.getM03() : 0;
            monthlyTotals[3] += dto.getM04() != null ? dto.getM04() : 0;
            monthlyTotals[4] += dto.getM05() != null ? dto.getM05() : 0;
            monthlyTotals[5] += dto.getM06() != null ? dto.getM06() : 0;
            monthlyTotals[6] += dto.getM07() != null ? dto.getM07() : 0;
            monthlyTotals[7] += dto.getM08() != null ? dto.getM08() : 0;
            monthlyTotals[8] += dto.getM09() != null ? dto.getM09() : 0;
            monthlyTotals[9] += dto.getM10() != null ? dto.getM10() : 0;
            monthlyTotals[10] += dto.getM11() != null ? dto.getM11() : 0;
            monthlyTotals[11] += dto.getM12() != null ? dto.getM12() : 0;
        }

        ContractDto totalDto = new ContractDto();
        totalDto.setContractName("합계");
        totalDto.setTotalAmount(totalAmount);
        totalDto.setM01(monthlyTotals[0]);
        totalDto.setM02(monthlyTotals[1]);
        totalDto.setM03(monthlyTotals[2]);
        totalDto.setM04(monthlyTotals[3]);
        totalDto.setM05(monthlyTotals[4]);
        totalDto.setM06(monthlyTotals[5]);
        totalDto.setM07(monthlyTotals[6]);
        totalDto.setM08(monthlyTotals[7]);
        totalDto.setM09(monthlyTotals[8]);
        totalDto.setM10(monthlyTotals[9]);
        totalDto.setM11(monthlyTotals[10]);
        totalDto.setM12(monthlyTotals[11]);

        list.add(totalDto);
    }
}
