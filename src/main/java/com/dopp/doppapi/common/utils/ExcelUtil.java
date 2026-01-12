package com.dopp.doppapi.common.utils;

import jakarta.servlet.http.HttpServletResponse;
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

    /**
     * 리스트 데이터를 엑셀 파일로 다운로드
     *
     * @param response  HttpServletResponse
     * @param dataList  데이터 리스트
     * @param headerMap 헤더 정보 (Key: 필드명, Value: 헤더명)
     * @param fileName  파일명
     * @param sheetName 시트명
     * @param <T>       데이터 타입
     */
    public static <T> void downloadExcel(HttpServletResponse response, List<T> dataList, Map<String, String> headerMap, String fileName, String sheetName) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet(sheetName);

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        createHeaderRow(sheet, headerMap, headerStyle);
        createDataRows(sheet, dataList, headerMap, dataStyle);
        adjustColumnWidths(sheet, headerMap.size());

        writeResponse(response, workbook, fileName);
    }

    // 오버로딩: 시트명 생략 시 기본값 사용
    public static <T> void downloadExcel(HttpServletResponse response, List<T> dataList, Map<String, String> headerMap, String fileName) throws IOException {
        downloadExcel(response, dataList, headerMap, fileName, "Sheet1");
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

    private static void setBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    private static void createHeaderRow(Sheet sheet, Map<String, String> headerMap, CellStyle style) {
        Row headerRow = sheet.createRow(0);
        int colIndex = 0;
        for (String headerName : headerMap.values()) {
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(headerName);
            cell.setCellStyle(style);
        }
    }

    private static <T> void createDataRows(Sheet sheet, List<T> dataList, Map<String, String> headerMap, CellStyle style) {
        int rowIndex = 1;
        for (T data : dataList) {
            Row row = sheet.createRow(rowIndex++);
            int colIndex = 0;
            for (String fieldName : headerMap.keySet()) {
                Cell cell = row.createCell(colIndex++);
                cell.setCellStyle(style);
                setCellValue(cell, data, fieldName);
            }
        }
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

    // 상위 클래스 필드까지 검색하도록 개선
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
