package com.dopp.doppapi.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelUtil {

    private static final int MIN_COLUMN_WIDTH = 3000;
    private static final int MAX_COLUMN_WIDTH = 20000;

    @Getter
    @AllArgsConstructor
    public static class ExcelTableData<T> {
        private List<T> dataList;
        private Map<String, String> headerMap;
        private String title; // 테이블 제목 (선택 사항)
    }

    /**
     * 단일 시트 엑셀 다운로드
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
}
