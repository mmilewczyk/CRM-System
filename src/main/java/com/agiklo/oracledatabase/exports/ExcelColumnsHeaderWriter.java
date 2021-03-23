package com.agiklo.oracledatabase.exports;

import org.apache.poi.ss.usermodel.*;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
public interface ExcelColumnsHeaderWriter {

    /**
     * The method is responsible for creating and styling the column headings accordingly
     * @param workbook the workbook to be stylized
     * @param sheet the sheet to be stylized
     * @param columns an array with the contents of the column headings in the table
     */
    default void writeColumnsHeader(Workbook workbook, Sheet sheet, String[] columns){
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.GREY_40_PERCENT.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }
}
