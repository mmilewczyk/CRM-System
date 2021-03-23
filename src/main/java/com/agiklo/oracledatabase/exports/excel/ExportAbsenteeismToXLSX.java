package com.agiklo.oracledatabase.exports.excel;

import com.agiklo.oracledatabase.entity.Absenteeism;
import com.agiklo.oracledatabase.exports.ExcelColumnsHeaderWriter;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
@AllArgsConstructor
public class ExportAbsenteeismToXLSX implements ExcelColumnsHeaderWriter {

    /**
     * an array that stores the content of the headers in columns
     */
    private static final String[] columns = {"Id", "Employee Id", "First Name", "Last Name", "Department", "Date from", "Date to", "Reasons"};
    private final List<Absenteeism> absenteeisms;

    /**
     * The method completes the sheet with data
     * @param sheet sheet to be filled with data
     */
    private void writeCellsData(Sheet sheet) {
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

    /**
     * The method allows you to create a file and export it
     * @param response response responsible for the ability to download the file
     * @throws IOException exception thrown in case of erroneous data
     */
    public void export(HttpServletResponse response) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Workbook workbook = new XSSFWorkbook();
        String currentDateTime = dateFormatter.format(new Date());
        String headerValue = "absenteeisms_" + currentDateTime + ".xlsx";
        Sheet sheet = workbook.createSheet(headerValue);

        writeColumnsHeader(workbook, sheet, columns);
        writeCellsData(sheet);

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
