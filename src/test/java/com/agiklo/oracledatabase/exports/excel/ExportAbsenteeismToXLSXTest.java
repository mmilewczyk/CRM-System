package com.agiklo.oracledatabase.exports.excel;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

class ExportAbsenteeismToXLSXTest {

    @Test
    void ResponseShouldeBeCommited() throws IOException {
        // given
        ExportAbsenteeismToXLSX exportAbsenteeismToXLSX = new ExportAbsenteeismToXLSX(new ArrayList<>());
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        // when
        exportAbsenteeismToXLSX.export(mockHttpServletResponse);

        // then
        assertTrue(mockHttpServletResponse.isCommitted());
    }
}

