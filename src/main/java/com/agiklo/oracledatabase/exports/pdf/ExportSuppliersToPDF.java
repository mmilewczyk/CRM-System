package com.agiklo.oracledatabase.exports.pdf;

import com.agiklo.oracledatabase.entity.Supplier;
import com.agiklo.oracledatabase.exports.ExportPDFRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class ExportSuppliersToPDF implements ExportPDFRepository {
    private List<Supplier> supplierList;

    @Override
    public void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.gray);
        cell.setPadding(5);

        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Name of Supplier", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Mode of transport", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Max length", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Max weight", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Min length", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Min weight", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Transport capacity", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Activity status", font));
        table.addCell(cell);
    }

    @Override
    public void writeTableData(PdfPTable table) {
        for (Supplier supplier : supplierList) {
            table.addCell(String.valueOf(supplier.getSupplierId()));
            table.addCell(String.valueOf(supplier.getSupplierName()));
            table.addCell(String.valueOf(supplier.getModeOfTransportCode().getFullName()));
            table.addCell(String.valueOf(supplier.getModeOfTransportCode().getMaxLength()));
            table.addCell(String.valueOf(supplier.getModeOfTransportCode().getMaxWeight()));
            table.addCell(String.valueOf(supplier.getModeOfTransportCode().getMinLength()));
            table.addCell(String.valueOf(supplier.getModeOfTransportCode().getMinWeight()));
            table.addCell(String.valueOf(supplier.getModeOfTransportCode().getTransportCapacity()));
            table.addCell(String.valueOf(supplier.getActivityStatus()));
        }
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        Paragraph date = new Paragraph("Date of data export: " + currentDateTime, FontFactory.getFont(FontFactory.TIMES_ROMAN));
        date.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(date);

        com.lowagie.text.Image jpg = com.lowagie.text.Image.getInstance("https://i.imgur.com/PpNT62x.jpg");
        jpg.setAlignment(Image.ALIGN_CENTER);
        jpg.scaleAbsolute(100f, 30.11f);
        document.add(jpg);

        Paragraph p = new Paragraph("Suppliers", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();

    }
}

