package com.agiklo.oracledatabase.exports.excel;

import com.agiklo.oracledatabase.entity.Customers;
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

@AllArgsConstructor
public class ExportCustomersToXLSX implements ExcelColumnsHeaderWriter {

    private static final String[] columns = {"Id", "First Name", "Last Name", "Zip Code", "City"};
    private final List<Customers> customersList;

    private void writeCellsData(Sheet sheet) {
        int rowNum = 1;
        for (Customers customer : customersList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(customer.getId());
            row.createCell(1).setCellValue(customer.getFirstname());
            row.createCell(2).setCellValue(customer.getLastname());
            row.createCell(3).setCellValue(customer.getZipCode());
            row.createCell(4).setCellValue(customer.getCity());
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
        String headerValue = "customers_" + currentDateTime + ".xlsx";
        Sheet sheet = workbook.createSheet(headerValue);

        writeColumnsHeader(workbook, sheet, columns);
        writeCellsData(sheet);

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
