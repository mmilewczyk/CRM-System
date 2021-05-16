package com.agiklo.oracledatabase.exports.pdf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lowagie.text.pdf.PdfPTable;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class ExportAbsenteeismToPDFTest {

    @Test
    void shouldWriteTableData() {
        // given
        ExportAbsenteeismToPDF exportAbsenteeismToPDF = new ExportAbsenteeismToPDF(new ArrayList<>());
        PdfPTable pdfPTable = new PdfPTable(10);

        // when
        exportAbsenteeismToPDF.writeTableData(pdfPTable);

        // then
        assertEquals(10, pdfPTable.getAbsoluteWidths().length);
        assertEquals(0.0f, pdfPTable.spacingBefore());
        assertEquals(0.0f, pdfPTable.spacingAfter());
        assertEquals(0, pdfPTable.size());
        assertTrue(pdfPTable.isSplitRows());
        assertTrue(pdfPTable.isSplitLate());
        assertFalse(pdfPTable.isSkipLastFooter());
        assertFalse(pdfPTable.isSkipFirstHeader());
        assertFalse(pdfPTable.isLockedWidth());
        assertFalse(pdfPTable.isHeadersInEvent());
        assertFalse(pdfPTable.isExtendLastRow());
        assertTrue(pdfPTable.isComplete());
        assertEquals(80.0f, pdfPTable.getWidthPercentage());
        assertEquals(0.0f, pdfPTable.getTotalWidth());
        assertEquals(0.0f, pdfPTable.getTotalHeight());
        assertEquals(0, pdfPTable.getRunDirection());
        assertEquals(10, pdfPTable.getNumberOfColumns());
        assertFalse(pdfPTable.getKeepTogether());
        assertEquals(1, pdfPTable.getHorizontalAlignment());
        assertEquals(0, pdfPTable.getHeaderRows());
        assertEquals(0, pdfPTable.getFooterRows());
    }
}

