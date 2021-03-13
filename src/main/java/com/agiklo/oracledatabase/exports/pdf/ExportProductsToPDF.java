package com.agiklo.oracledatabase.exports.pdf;

import com.agiklo.oracledatabase.entity.Product;
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
public class ExportProductsToPDF implements ExportPDFRepository, PDFFileDesignRepository {

    private final List<Product> productList;

    private static final String[] columns = {"Id", "Name of product", "Product type", "Selling price", "Purchase price", "Tax rate"};

    @Override
    public void writeTableData(PdfPTable table) {
        for (Product product : productList) {
            table.addCell(String.valueOf(product.getId()));
            table.addCell(product.getName());
            table.addCell(product.getProductType().getFullName());
            table.addCell(product.getSellingPrice().toString() + " " + CURRENCY.PLN.name());
            table.addCell(product.getPurchasePrice().toString() + " " + CURRENCY.PLN.name());
            table.addCell(product.getTaxRate().toString() + "%");
        }
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        setupFileStyle(response, document);

        Paragraph p = new Paragraph("Products", setupFont());
        p.setAlignment(ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.0f, 4.0f, 3.0f, 3.0f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table, columns);
        writeTableData(table);

        document.add(table);
        document.close();

    }
}
