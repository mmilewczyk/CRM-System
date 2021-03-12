package com.agiklo.oracledatabase.exports.excel;

import com.agiklo.oracledatabase.entity.Absenteeism;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class ExportAbsenteeismToXLSX {

    private static final String[] columns = {"Id", "Employee Id", "First Name", "Last Name", "Department", "Date from", "Date to", "Reasons"};
    List<Absenteeism> absenteeisms;

    public void writeColumnsHeader(Workbook workbook, Sheet sheet){
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

    public void writeCellsData(Sheet sheet) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        int rowNum = 1;
        for (Absenteeism absenteeism : absenteeisms) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(absenteeism.getId());
            row.createCell(1).setCellValue(absenteeism.getEmployee().getId());
            row.createCell(2).setCellValue(absenteeism.getEmployee().getFirstName());
            row.createCell(3).setCellValue(absenteeism.getEmployee().getLastName());
            row.createCell(4).setCellValue(absenteeism.getEmployee().getDepartment().getDepartmentName());
            row.createCell(5).setCellValue(dateFormatter.format(absenteeism.getDateFrom()));
            row.createCell(6).setCellValue(dateFormatter.format(absenteeism.getDateTo()));
            row.createCell(7).setCellValue(String.valueOf(absenteeism.getReasonOfAbsenteeismCode().getAbsenteeismName()));
            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Workbook workbook = new XSSFWorkbook();
        String currentDateTime = dateFormatter.format(new Date());
        String headerValue = "absenteeisms_" + currentDateTime + ".xlsx";
        Sheet sheet = workbook.createSheet(headerValue);

        writeColumnsHeader(workbook, sheet);
        writeCellsData(sheet);

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
