package com.agiklo.oracledatabase.exports.excel;

import com.agiklo.oracledatabase.entity.Product;
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
public class ExportProductsToXLSX implements ExcelColumnsHeaderWriter {

    private static final String[] columns = {"Id", "Name of product", "product type", "Selling price", "Purchase price", "Tax rate"};
    private final List<Product> products;

    private void writeCellsData(Sheet sheet) {
        int rowNum = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getProductType().getFullName());
            row.createCell(3).setCellValue(product.getSellingPrice());
            row.createCell(4).setCellValue(product.getPurchasePrice());
            row.createCell(5).setCellValue(product.getTaxRate());
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
        String headerValue = "products_" + currentDateTime + ".xlsx";
        Sheet sheet = workbook.createSheet(headerValue);

        writeColumnsHeader(workbook, sheet, columns);
        writeCellsData(sheet);

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
