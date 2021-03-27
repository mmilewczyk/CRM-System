package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.Purchases;
import com.agiklo.oracledatabase.entity.dto.PurchasesDTO;
import com.agiklo.oracledatabase.repository.PurchasesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class PurchasesServiceTest {

    @Autowired
    private PurchasesService purchasesService;
    @Autowired
    private PurchasesRepository purchasesRepository;

    @Test
    @Transactional
    void shouldGetSellingInvoiceById() {
        Purchases purchases = preparePurchaseToTest();
        purchasesRepository.save(purchases);
        //when
        PurchasesDTO purchasesDTO = purchasesService.getPurchaseById(purchases.getId());
        //then
        assertThat(purchasesDTO).isNotNull();
        assertThat(purchasesDTO.getCustomerFirstName()).isEqualTo("Vidkun");
        assertThat(purchasesDTO.getCustomerLastName()).isEqualTo("Rikardson");
    }

    Purchases preparePurchaseToTest() {
        Customers customer = new Customers();
        customer.setFirstname("Vidkun");
        customer.setLastname("Rikardson");
        customer.setPesel("97542899482");
        customer.setCity("Oslo");
        customer.setZipCode("300-20");

        Purchases purchases = new Purchases();
        purchases.setCustomer(customer);
        return purchases;
    }
}
