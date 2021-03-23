package com.agiklo.oracledatabase.exports.excel;

import com.agiklo.oracledatabase.entity.Departments;
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
public class ExportDepartmentsToXLSX implements ExcelColumnsHeaderWriter {

    /**
     * an array that stores the content of the headers in columns
     */
    private static final String[] columns = {"Id", "Department name", "City", "Manager"};
    private final List<Departments> departmentsList;

    /**
     * The method completes the sheet with data
     * @param sheet sheet to be filled with data
     */
    private void writeCellsData(Sheet sheet) {
        int rowNum = 1;
        for (Departments department : departmentsList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(department.getId());
            row.createCell(1).setCellValue(department.getDepartmentName());
            row.createCell(2).setCellValue(department.getCity());
            row.createCell(3).setCellValue(
                    department.getManagers().getFirstName() + " " + department.getManagers().getLastName());
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
        Workbook workbook = new XSSFWorkbook();

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        String headerValue = "departments_" + currentDateTime + ".xlsx";
        Sheet sheet = workbook.createSheet(headerValue);

        writeColumnsHeader(workbook, sheet, columns);
        writeCellsData(sheet);

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
