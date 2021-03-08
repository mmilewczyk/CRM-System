package com.agiklo.oracledatabase.exports;

import com.agiklo.oracledatabase.entity.Customers;
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
public class ExportCustomersToPDF implements ExportRepository{
    private List<Customers> customersList;

    @Override
    public void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.gray);
        cell.setPadding(5);

        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Zip code", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("City", font));
        table.addCell(cell);
    }

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
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
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

        Paragraph p = new Paragraph("Customers", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();

    }
}

