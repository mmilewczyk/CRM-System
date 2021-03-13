package com.agiklo.oracledatabase.exports.excel;

import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.exports.ExcelColumnsHeaderWriter;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class ExportEmployeeToXLSX implements ExcelColumnsHeaderWriter {
    private static final String[] columns = {"Id", "First Name", "Last Name", "Department", "Role", "Salary"};
    private final List<Employee> employees;

    private void writeCellsData(Sheet sheet) {
        int rowNum = 1;
        for (Employee employee : employees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getId());
            row.createCell(1).setCellValue(employee.getFirstName());
            row.createCell(2).setCellValue(employee.getLastName());
            row.createCell(3).setCellValue(employee.getDepartment().getDepartmentName());
            row.createCell(4).setCellValue(employee.getUserRole().toString());
            row.createCell(5).setCellValue(employee.getSalary());
            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH");
        String currentDateTime = dateFormatter.format(new Date());
        String headerValue = "employees_" + currentDateTime + ".xlsx";
        Sheet sheet = workbook.createSheet(headerValue);

        writeColumnsHeader(workbook, sheet, columns);
        writeCellsData(sheet);

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
