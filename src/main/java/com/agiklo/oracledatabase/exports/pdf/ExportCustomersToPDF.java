package com.agiklo.oracledatabase.exports.pdf;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.exports.ExportPDFRepository;
import com.agiklo.oracledatabase.exports.PDFFileDesignRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import lombok.AllArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import static com.lowagie.text.Element.ALIGN_CENTER;

@AllArgsConstructor
public class ExportCustomersToPDF implements ExportPDFRepository, PDFFileDesignRepository {

    private final List<Customers> customersList;

    private static final String[] columns = {"Id", "First Name", "Last Name", "Zip code", "City"};

    @Override
    public void writeTableData(PdfPTable table) {
        for (Customers customers : customersList) {
            table.addCell(String.valueOf(customers.getId()));
            table.addCell(String.valueOf(customers.getFirstname()));
            table.addCell(String.valueOf(customers.getLastname()));
            table.addCell(String.valueOf(customers.getZipCode()));
            table.addCell(String.valueOf(customers.getCity()));
        }
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        setupFileStyle(response, document);

        Paragraph p = new Paragraph("Customers", setupFont());
        p.setAlignment(ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table, columns);
        writeTableData(table);

        document.add(table);
        document.close();

    }
}

