package com.agiklo.oracledatabase.exports.pdf;

import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.enums.CURRENCY;
import com.agiklo.oracledatabase.exports.ExportPDFRepository;
import com.agiklo.oracledatabase.exports.PDFFileDesignRepository;
import com.lowagie.text.pdf.PdfPTable;

import java.util.List;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import lombok.AllArgsConstructor;
import static com.lowagie.text.Element.ALIGN_CENTER;

@AllArgsConstructor
public class ExportEmployeeToPDF implements ExportPDFRepository, PDFFileDesignRepository {

    private final List<Employee> employeesList;

    private static final String[] columns = {"First Name", "Last Name", "Department", "Salary"};

    @Override
    public void writeTableData(PdfPTable table) {
        for (Employee employee : employeesList) {
            table.addCell(String.valueOf(employee.getFirstName()));
            table.addCell(employee.getLastName());
            table.addCell(employee.getDepartment().getDepartmentName());
            table.addCell(employee.getSalary().toString() + " " + CURRENCY.PLN.name());
        }
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        setupFileStyle(response, document);

        Paragraph p = new Paragraph("Employees", setupFont());
        p.setAlignment(ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table, columns);
        writeTableData(table);

        document.add(table);
        document.close();

    }
}
