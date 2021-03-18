package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import com.agiklo.oracledatabase.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Transactional
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldGetCustomerById() throws Exception {
        //given
        Customers newCustomer = new Customers();
        newCustomer.setFirstname("Mateusz");
        newCustomer.setLastname("Milewczyk");
        newCustomer.setCity("Oslo");
        newCustomer.setZipCode("300-20");
        customerRepository.save(newCustomer);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/" + newCustomer.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //.andExpect(jsonPath("$.firstname", Matchers.is("Kazimiera")));
        //then
        CustomerDTO customer = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerDTO.class);
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstname()).isEqualTo("Mateusz");
        assertThat(customer.getCity()).isEqualTo("Oslo");
    }
}