package com.agiklo.oracledatabase.exports;

import com.agiklo.oracledatabase.entity.Absenteeism;;
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
public class ExportAbsenteeismToPDF implements ExportPDFRepository{
    private final List<Absenteeism> absenteeismList;

    @Override
    public void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.gray);
        cell.setPadding(5);

        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Employee id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Employee name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Department", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date from", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date to", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Reasons", font));
        table.addCell(cell);
    }

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

        Paragraph p = new Paragraph("Absenteeisms", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.0f, 4.0f, 4.0f, 3.2f, 3.2f, 4.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();

    }
}
