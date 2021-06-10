package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.*;
import com.agiklo.oracledatabase.repository.PurchasesPositionsRepository;
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
class PurchasesPositionsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PurchasesPositionsRepository purchasesPositionsRepository;

    @Test
    void shouldNoGetPurchasesPositionsAndReturnForbridden() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/purchases-positions"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Access Denied");
    }

    @Test
    @WithMockUser(username = "ofiabrzydal@agiklocrm.comz", password = "123", authorities = "EMPLOYEE")
    void shouldNoGetPurchasesPositionById() throws Exception {
        //given
        List<PurchasesPositions> purchasesPositions = purchasesPositionsRepository.findAll();
        long fakeId;
        if (purchasesPositions.isEmpty()) {
            fakeId = 1;}
        else {
            fakeId = purchasesPositions.stream()
                    .mapToLong(PurchasesPositions::getId)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
        }
        fakeId++;
        //when
        mockMvc.perform(get("/api/v1/purchases-positions/" + fakeId)).andDo(print())
                //then
                .andExpect(status().is(404))
                .andReturn();
    }

    PurchasesPositions preparePurchasesPositionToTest(){
        Product product = new Product();
        product.setName("Tofu");
        product.setProductType(new ProductType("Protein", 0.5, 'K'));
        product.setPurchasePrice(3.99);
        product.setSellingPrice(5.00);

        Customers customer = new Customers();
        customer.setFirstname("Vidkun");
        customer.setLastname("Rikardson");
        customer.setPesel("97542899482");
        customer.setCity("Oslo");
        customer.setZipCode("300-20");

        Purchases purchases = new Purchases();
        purchases.setCustomer(customer);

        PurchasesPositions newPurchasesPosition = new PurchasesPositions();
        newPurchasesPosition.setProduct(product);
        newPurchasesPosition.setPurchases(purchases);
        newPurchasesPosition.setAmount(21D);
        newPurchasesPosition.setReclamationExist('N');
        return newPurchasesPosition;
    }
}
