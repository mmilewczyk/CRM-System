package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.Purchases;
import com.agiklo.oracledatabase.entity.dto.PurchasesDTO;
import com.agiklo.oracledatabase.repository.PurchasesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class PurchasesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PurchasesRepository purchasesRepository;

    @Test
    void shouldNoGetPurchasesAndReturnForbridden() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/purchases"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Access Denied");
    }

    @Test
    @Transactional
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldGetPurchaseById() throws Exception {
        //given
        Purchases fakePurchases = preparePurchaseToTest();
        purchasesRepository.save(fakePurchases);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/purchases/" + fakePurchases.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        PurchasesDTO purchases = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PurchasesDTO.class);
        assertThat(purchases).isNotNull();
        assertThat(purchases.getCustomerFirstName()).isEqualTo("Vidkun");
    }

    @Test
    @WithMockUser(username = "ofiabrzydal@agiklocrm.comz", password = "123", authorities = "EMPLOYEE")
    void shouldNoGetPurchaseById() throws Exception {
        //given
        List<Purchases> purchases = purchasesRepository.findAll();
        long fakeId;
        if (purchases.isEmpty()) {
            fakeId = 1;}
        else {
            fakeId = purchases.stream()
                    .mapToLong(Purchases::getId)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
        }
        fakeId++;
        //when
        mockMvc.perform(get("/api/v1/purchases/" + fakeId)).andDo(print())
                //then
                .andExpect(status().is(404))
                .andReturn();
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
