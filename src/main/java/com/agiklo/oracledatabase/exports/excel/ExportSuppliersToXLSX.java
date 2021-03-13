package com.agiklo.oracledatabase.exports.excel;

import com.agiklo.oracledatabase.entity.Supplier;
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
public class ExportSuppliersToXLSX implements ExcelColumnsHeaderWriter {

    private static final String[] columns =
            {"Id", "Name of Supplier", "Mode of transport", "Min Length", "Max Length", "Min Weight", "Max Weight", "Transport Capacity", "Activity Status"};
    private final List<Supplier> suppliers;

    private void writeCellsData(Sheet sheet) {
        int rowNum = 1;
        for (Supplier supplier : suppliers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(supplier.getSupplierId());
            row.createCell(1).setCellValue(supplier.getSupplierName());
            row.createCell(2).setCellValue(supplier.getModeOfTransportCode().getFullName());
            row.createCell(3).setCellValue(supplier.getModeOfTransportCode().getMinLength());
            row.createCell(4).setCellValue(supplier.getModeOfTransportCode().getMaxLength());
            row.createCell(5).setCellValue(supplier.getModeOfTransportCode().getMinWeight());
            row.createCell(6).setCellValue(supplier.getModeOfTransportCode().getMaxWeight());
            row.createCell(7).setCellValue(supplier.getModeOfTransportCode().getTransportCapacity());
            row.createCell(8).setCellValue(supplier.getActivityStatus());
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
        String headerValue = "suppliers_" + currentDateTime + ".xlsx";
        Sheet sheet = workbook.createSheet(headerValue);

        writeColumnsHeader(workbook, sheet, columns);
        writeCellsData(sheet);

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
