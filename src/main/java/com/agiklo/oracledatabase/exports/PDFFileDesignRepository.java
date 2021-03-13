package com.agiklo.oracledatabase.exports;

import com.agiklo.oracledatabase.service.CurrentTimeInterface;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

import static com.lowagie.text.Element.ALIGN_CENTER;
import static com.lowagie.text.Element.ALIGN_RIGHT;

public interface PDFFileDesignRepository extends CurrentTimeInterface {

    default void setupFileStyle(HttpServletResponse response, Document document) throws IOException {
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Paragraph date = new Paragraph("Date of data export: " + getCurrentDateTime(), FontFactory.getFont(FontFactory.TIMES_ROMAN));
        date.setAlignment(ALIGN_RIGHT);
        document.add(date);

        com.lowagie.text.Image jpg = com.lowagie.text.Image.getInstance("https://i.imgur.com/PpNT62x.jpg");
        jpg.setAlignment(ALIGN_CENTER);
        jpg.scaleAbsolute(100f, 30.11f);
        document.add(jpg);
    }

    default Font setupFont(){
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);
        return font;
    }
}
