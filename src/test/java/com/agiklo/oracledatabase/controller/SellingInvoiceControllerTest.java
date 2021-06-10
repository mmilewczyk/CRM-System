package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.SellingInvoice;
import com.agiklo.oracledatabase.entity.dto.SellingInvoiceDTO;
import com.agiklo.oracledatabase.enums.CURRENCY;
import com.agiklo.oracledatabase.repository.SellingInvoiceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Execution(ExecutionMode.CONCURRENT)
class SellingInvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SellingInvoiceRepository sellingInvoiceRepository;

    @Test
    void shouldNoGetSellingInvoicesAndReturnForbridden() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/invoices"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Access Denied");
    }

    @Test
    @Transactional
    @Disabled
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldGetInvoiceById() throws Exception {
        //given
        SellingInvoice fakeInvoice = prepareInvoiceToTest();
        sellingInvoiceRepository.save(fakeInvoice);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/invoices/" + fakeInvoice.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        SellingInvoiceDTO sellingInvoice = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SellingInvoiceDTO.class);
        assertThat(sellingInvoice).isNotNull();
        assertThat(sellingInvoice.getCustomerFirstName()).isEqualTo("Vidkun");
        assertThat(sellingInvoice.getCustomerLastName()).isEqualTo("Rikardson");
        assertThat(sellingInvoice.getCurrency()).isEqualTo(CURRENCY.GBP);
        assertThat(sellingInvoice.getGrossValue()).isEqualTo(62.5);
        assertThat(sellingInvoice.getNetWorth()).isEqualTo(54.3);
        assertThat(sellingInvoice.getTaxRate()).isEqualTo(2.9);
    }

    @Test
    @WithMockUser(username = "ofiabrzydal@agiklocrm.comz", password = "123", authorities = "EMPLOYEE")
    void shouldNoGetInvoiceById() throws Exception {
        //given
        List<SellingInvoice> sellingInvoices = sellingInvoiceRepository.findAll();
        long fakeId;
        if (sellingInvoices.isEmpty()) {
            fakeId = 1;}
        else {
            fakeId = sellingInvoices.stream()
                    .mapToLong(SellingInvoice::getId)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
        }
        fakeId++;
        //when
        mockMvc.perform(get("/api/v1/invoices/" + fakeId)).andDo(print())
                //then
                .andExpect(status().is(404))
                .andReturn();
    }

    SellingInvoice prepareInvoiceToTest() {
        SellingInvoice newInvoice = new SellingInvoice();
        newInvoice.setCustomer(new Customers(
                "Vidkun",
                "Rikardson",
                "97542899482",
                "Oslo",
                "300-20"));
        newInvoice.setCurrency(CURRENCY.GBP);
        newInvoice.setGrossValue(62.5);
        newInvoice.setNetWorth(54.3);
        newInvoice.setTaxRate(2.9);
        newInvoice.setInvoiceDate(Date.valueOf(LocalDate.now()));
        return newInvoice;
    }
}
