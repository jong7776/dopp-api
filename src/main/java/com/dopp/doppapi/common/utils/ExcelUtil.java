package com.dopp.doppapi.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 엑셀 파일 다운로드 및 업로드를 위한 유틸리티 클래스
 */
@Slf4j
public class ExcelUtil {

    private static final int MIN_COLUMN_WIDTH = 3000;
    private static final int MAX_COLUMN_WIDTH = 20000;

    /**
     * 엑셀 테이블 데이터를 담는 클래스
     * @param <T> 데이터 타입
     */
    @Getter
    @AllArgsConstructor
    public static class ExcelTableData<T> {
        private List<T> dataList;
        private Map<String, String> headerMap;
        private String title; // 테이블 제목 (선택 사항)
    }

    /**
     * 단일 시트 엑셀 다운로드
     *
     * @param response  HttpServletResponse
     * @param dataList  데이터 리스트
     * @param headerMap 헤더 맵 (Key: 필드명, Value: 헤더명)
     * @param fileName  파일명
     * @param sheetName 시트명
     * @param <T>       데이터 타입
     * @throws IOException IO 예외
     */
    public static <T> void downloadExcel(HttpServletResponse response, List<T> dataList, Map<String, String> headerMap, String fileName, String sheetName) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            SXSSFSheet sheet = workbook.createSheet(sheetName);
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            createHeaderRow(sheet, headerMap, headerStyle, 0);
            createDataRows(sheet, dataList, headerMap, dataStyle, 1);
            adjustColumnWidths(sheet, headerMap.size());

            writeResponse(response, workbook, fileName);
        }
    }

    /**
     * 단일 시트 엑셀 다운로드 (기본 시트명 "Sheet1" 사용)
     *
     * @param response  HttpServletResponse
     * @param dataList  데이터 리스트
     * @param headerMap 헤더 맵 (Key: 필드명, Value: 헤더명)
     * @param fileName  파일명
     * @param <T>       데이터 타입
     * @throws IOException IO 예외
     */
    public static <T> void downloadExcel(HttpServletResponse response, List<T> dataList, Map<String, String> headerMap, String fileName) throws IOException {
        downloadExcel(response, dataList, headerMap, fileName, "Sheet1");
    }

    /**
     * 한 시트에 여러 테이블 다운로드
     *
     * @param response      HttpServletResponse
     * @param tableDataList 테이블 데이터 리스트
     * @param fileName      파일명
     * @param sheetName     시트명
     * @throws IOException IO 예외
     */
    public static void downloadMultiTableExcel(HttpServletResponse response, List<ExcelTableData<?>> tableDataList, String fileName, String sheetName) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            SXSSFSheet sheet = workbook.createSheet(sheetName);

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);

            int currentRowIndex = 0;

            for (ExcelTableData<?> tableData : tableDataList) {
                // 테이블 제목 출력 (있는 경우)
                if (tableData.getTitle() != null && !tableData.getTitle().isEmpty()) {
                    Row titleRow = sheet.createRow(currentRowIndex++);
                    Cell titleCell = titleRow.createCell(0);
                    titleCell.setCellValue(tableData.getTitle());
                    titleCell.setCellStyle(titleStyle);
                    currentRowIndex++; // 제목과 테이블 사이 공백
                }

                // 헤더 생성
                createHeaderRow(sheet, tableData.getHeaderMap(), headerStyle, currentRowIndex++);

                // 데이터 생성
                int dataRows = createDataRows(sheet, tableData.getDataList(), tableData.getHeaderMap(), dataStyle, currentRowIndex);
                currentRowIndex += dataRows;

                // 테이블 간 간격 추가
                currentRowIndex += 2;
            }

            // 첫 번째 테이블 기준으로 컬럼 너비 조절 (모든 테이블의 컬럼 수가 같다고 가정하거나, 가장 긴 것 기준)
            if (!tableDataList.isEmpty()) {
                adjustColumnWidths(sheet, tableDataList.get(0).getHeaderMap().size());
            }

            writeResponse(response, workbook, fileName);
        }
    }

    /**
     * 엑셀 파일 업로드 및 데이터 파싱 (컬럼 순서 기반)
     * 헤더 행(0번째 행)은 건너뛰고, 1번째 행부터 데이터를 읽습니다.
     * headerMap의 Key 순서대로 엑셀의 0번째 열부터 매핑합니다.
     *
     * @param file       업로드된 엑셀 파일
     * @param clazz      매핑할 DTO 클래스
     * @param headerMap  DTO 필드명과 엑셀 헤더명 매핑 (순서가 중요함: LinkedHashMap 사용 권장)
     * @param <T>        DTO 타입
     * @return 파싱된 데이터 리스트
     * @throws IOException IO 예외
     */
    public static <T> List<T> uploadExcelByColumnOrder(MultipartFile file, Class<T> clazz, Map<String, String> headerMap) throws IOException {
        List<T> dataList = new ArrayList<>();

        // headerMap의 Key(필드명)들을 리스트로 변환하여 인덱스로 접근 가능하게 함
        List<String> fieldNames = new ArrayList<>(headerMap.keySet());

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트만 읽음

            int rowCount = sheet.getLastRowNum();
            // 0번째 행은 헤더로 간주하고 1번째 행부터 시작
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    T instance = clazz.getDeclaredConstructor().newInstance();
                    boolean isEmptyRow = true;

                    // 필드 순서대로 엑셀 컬럼 값을 매핑
                    for (int colIndex = 0; colIndex < fieldNames.size(); colIndex++) {
                        String fieldName = fieldNames.get(colIndex);
                        Cell cell = row.getCell(colIndex);
                        String cellValue = getCellValueAsString(cell);

                        if (cellValue != null && !cellValue.trim().isEmpty()) {
                            isEmptyRow = false;
                            setFieldValue(instance, fieldName, cellValue);
                        }
                    }

                    if (!isEmptyRow) {
                        dataList.add(instance);
                    }
                } catch (Exception e) {
                    log.error("Row {} parsing error: {}", i, e.getMessage());
                }
            }
        }

        return dataList;
    }

    // --- Private Helper Methods ---

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        setBorders(style);
        return style;
    }

    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setBorders(style);
        return style;
    }

    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        return style;
    }

    private static void setBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    private static void createHeaderRow(Sheet sheet, Map<String, String> headerMap, CellStyle style, int rowIndex) {
        Row headerRow = sheet.createRow(rowIndex);
        int colIndex = 0;
        for (String headerName : headerMap.values()) {
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(headerName);
            cell.setCellStyle(style);
        }
    }

    private static <T> int createDataRows(Sheet sheet, List<T> dataList, Map<String, String> headerMap, CellStyle style, int startRowIndex) {
        int rowIndex = startRowIndex;
        for (T data : dataList) {
            Row row = sheet.createRow(rowIndex++);
            int colIndex = 0;
            for (String fieldName : headerMap.keySet()) {
                Cell cell = row.createCell(colIndex++);
                cell.setCellStyle(style);
                setCellValue(cell, data, fieldName);
            }
        }
        return dataList.size();
    }

    private static <T> void setCellValue(Cell cell, T data, String fieldName) {
        try {
            Field field = getField(data.getClass(), fieldName);
            field.setAccessible(true);
            Object value = field.get(data);
            if (value != null) {
                cell.setCellValue(value.toString());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("======> Field access error: {}", fieldName, e);
            cell.setCellValue("");
        }
    }

    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            }
            return getField(superClass, fieldName);
        }
    }

    private static void adjustColumnWidths(SXSSFSheet sheet, int columnCount) {
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            int currentWidth = sheet.getColumnWidth(i);
            if (currentWidth < MIN_COLUMN_WIDTH) {
                sheet.setColumnWidth(i, MIN_COLUMN_WIDTH);
            } else if (currentWidth > MAX_COLUMN_WIDTH) {
                sheet.setColumnWidth(i, MAX_COLUMN_WIDTH);
            }
        }
    }

    private static void writeResponse(HttpServletResponse response, Workbook workbook, String fileName) throws IOException {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + ".xlsx\"");
        workbook.write(response.getOutputStream());
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                // 정수형 처리 (소수점 .0 제거)
                double numericValue = cell.getNumericCellValue();
                if (numericValue == (long) numericValue) {
                    return String.valueOf((long) numericValue);
                }
                return String.valueOf(numericValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private static <T> void setFieldValue(T instance, String fieldName, String value) {
        try {
            Field field = getField(instance.getClass(), fieldName);
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            if (fieldType == String.class) {
                field.set(instance, value);
            } else if (fieldType == Integer.class || fieldType == int.class) {
                field.set(instance, (int) Double.parseDouble(value));
            } else if (fieldType == Long.class || fieldType == long.class) {
                field.set(instance, (long) Double.parseDouble(value));
            } else if (fieldType == Double.class || fieldType == double.class) {
                field.set(instance, Double.parseDouble(value));
            } else if (fieldType == LocalDate.class) {
                // 날짜 형식 파싱 (필요에 따라 포맷터 추가)
                try {
                    field.set(instance, LocalDate.parse(value.substring(0, 10)));
                } catch (Exception e) {
                     // 엑셀 날짜가 LocalDateTime.toString() 형태로 넘어올 경우 처리
                     try {
                         field.set(instance, LocalDateTime.parse(value).toLocalDate());
                     } catch (Exception ex) {
                         log.warn("Date parsing failed for field {}: {}", fieldName, value);
                     }
                }
            } else if (fieldType == LocalDateTime.class) {
                 try {
                    field.set(instance, LocalDateTime.parse(value));
                } catch (Exception e) {
                     log.warn("DateTime parsing failed for field {}: {}", fieldName, value);
                }
            }
        } catch (Exception e) {
            log.warn("Field setting failed: {} = {} ({})", fieldName, value, e.getMessage());
        }
    }
}
