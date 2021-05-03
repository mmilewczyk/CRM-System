package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.SellingInvoice;
import com.agiklo.oracledatabase.entity.dto.SellingInvoiceDTO;
import com.agiklo.oracledatabase.enums.CURRENCY;
import com.agiklo.oracledatabase.repository.SellingInvoiceRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class SellingInvoiceServiceTest {

    @Autowired
    private SellingInvoiceService sellingInvoiceService;
    @Autowired
    private SellingInvoiceRepository sellingInvoiceRepository;

    @Test
    @Transactional
    @Disabled
    void shouldGetSellingInvoiceById() {
        SellingInvoice sellingInvoice = prepareInvoiceToTest();
        sellingInvoiceRepository.save(sellingInvoice);
        //when
        SellingInvoiceDTO sellingInvoiceDTO = sellingInvoiceService.getInvoiceById(sellingInvoice.getId());
        //then
        assertThat(sellingInvoiceDTO).isNotNull();
        assertThat(sellingInvoiceDTO.getCurrency()).isEqualTo("GBP");
        assertThat(sellingInvoiceDTO.getNetWorth()).isEqualTo(54.3);
    }

    SellingInvoice prepareInvoiceToTest() {
        Customers newCustomer = new Customers();
        newCustomer.setFirstname("Vidkun");
        newCustomer.setLastname("Rikardson");
        newCustomer.setPesel("97542899482");
        newCustomer.setCity("Oslo");
        newCustomer.setZipCode("300-20");

        SellingInvoice newInvoice = new SellingInvoice();
        newInvoice.setCustomer(newCustomer);
        newInvoice.setCurrency(CURRENCY.GBP);
        newInvoice.setGrossValue(62.5);
        newInvoice.setNetWorth(54.3);
        newInvoice.setTaxRate(2.9);
        newInvoice.setInvoiceDate(Date.valueOf(LocalDate.now()));
        return newInvoice;
    }
}
