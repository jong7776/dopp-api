package com.dopp.doppapi.service.common;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ExcelHeaderMapService {

    public Map<String, String> getContractListHeaderMap(boolean isDownload) {
        Map<String, String> headerMap = new LinkedHashMap<>();

        if (isDownload) {
            headerMap.put("year", "연도");
            headerMap.put("contractName", "계약명");
            headerMap.put("companyName", "업체명");
            headerMap.put("contractStart", "계약 시작일");
            headerMap.put("contractEnd", "계약 종료일");
            headerMap.put("invoiceRule", "계산서 발행 기준"); // (선금/잔금, 매월 말일 등)
            headerMap.put("paymentTerm", "지급 조건"); //  (예: 30일 이내)
            headerMap.put("totalAmount", "총액");
        } else {
            headerMap.put("type", "계약 구분 (S: 매출, P: 매입)");
            headerMap.put("year", "적용 연도");
            headerMap.put("contractName", "계약명");
            headerMap.put("companyName", "업체명");
            headerMap.put("contractStart", "계약 시작일");
            headerMap.put("contractEnd", "계약 종료일");
            headerMap.put("invoiceRule", "계산서 발행 기준"); // (선금/잔금, 매월 말일 등)
            headerMap.put("paymentTerm", "지급 조건"); //  (예: 30일 이내)
        }

        // 월별 컬럼 공통 추가
        for (int i = 1; i <= 12; i++) {
            String key = String.format("m%02d", i);
            String value = isDownload ? i + "월" : i + "월 금액";
            headerMap.put(key, value);
        }
        return headerMap;
    }

    public Map<String, String> getExpenseListHeaderMap(boolean isDownload) {
        Map<String, String> headerMap = new LinkedHashMap<>();
        headerMap.put("expenseName", "경비 내역");
        headerMap.put("year", "연도");

        if (isDownload) {
            headerMap.put("totalAmount", "총액");
        }

        // 월별 컬럼 공통 추가
        for (int i = 1; i <= 12; i++) {
            String key = String.format("m%02d", i);
            String value = isDownload ? i + "월" : i + "월 금액";
            headerMap.put(key, value);
        }
        return headerMap;
    }
}
