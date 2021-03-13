package com.agiklo.oracledatabase.exports.pdf;

import com.agiklo.oracledatabase.entity.Absenteeism;
import com.agiklo.oracledatabase.exports.ExportPDFRepository;
import com.agiklo.oracledatabase.exports.PDFFileDesignRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.lowagie.text.Element.ALIGN_CENTER;

@AllArgsConstructor
public class ExportAbsenteeismToPDF implements ExportPDFRepository, PDFFileDesignRepository {
    private final List<Absenteeism> absenteeismList;

    private static final String[] columns = {"Id", "Employee id", "Employee name", "Department", "Date from", "Date to", "Reasons"};

    @Override
    public void writeTableData(PdfPTable table) {
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        for (Absenteeism absenteeism : absenteeismList) {
            table.addCell(String.valueOf(absenteeism.getId()));
            table.addCell(String.valueOf(absenteeism.getEmployee().getId()));
            table.addCell(absenteeism.getEmployee().getFirstName() + " " + absenteeism.getEmployee().getLastName());
            table.addCell(String.valueOf(absenteeism.getEmployee().getDepartment().getDepartmentName()));
            table.addCell(dateFormatter.format(absenteeism.getDateFrom()));
            table.addCell(dateFormatter.format(absenteeism.getDateTo()));
            table.addCell(String.valueOf(absenteeism.getReasonOfAbsenteeismCode().getAbsenteeismName()));
        }
    }

    @Override
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        setupFileStyle(response, document);

        Paragraph p = new Paragraph("Absenteeisms", setupFont());
        p.setAlignment(ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.0f, 4.0f, 4.0f, 3.2f, 3.2f, 4.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table, columns);
        writeTableData(table);

        document.add(table);
        document.close();

    }
}
