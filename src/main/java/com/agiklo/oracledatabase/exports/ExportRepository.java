package com.agiklo.oracledatabase.exports;

import com.lowagie.text.pdf.PdfPTable;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ExportRepository {

    void writeTableHeader(PdfPTable table);
    void writeTableData(PdfPTable table);
    void export(HttpServletResponse response) throws IOException;
}
