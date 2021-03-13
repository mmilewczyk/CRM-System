package com.agiklo.oracledatabase.exports;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import javax.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;

import static com.lowagie.text.FontFactory.HELVETICA;

public interface ExportPDFRepository {

    default void writeTableHeader(PdfPTable table, String[] columns) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.gray);
        cell.setPadding(5);

        com.lowagie.text.Font font = FontFactory.getFont(HELVETICA);
        font.setColor(Color.WHITE);

        for (String column : columns) {
            cell.setPhrase(new Phrase(column, font));
            table.addCell(cell);
        }
    }

    void writeTableData(PdfPTable table);
    void export(HttpServletResponse response) throws IOException;
}
