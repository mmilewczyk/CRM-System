package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Customers;
import com.agiklo.oracledatabase.entity.dto.CustomerDTO;
import com.agiklo.oracledatabase.repository.CustomerRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Execution(ExecutionMode.CONCURRENT)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldNoGetCustomersAndReturnForbridden() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Access Denied");
    }

    @Test
    @Transactional
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldGetCustomerById() throws Exception {
        //given
        Customers fakeCustomer = prepareCustomerToTest();
        customerRepository.save(fakeCustomer);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/" + fakeCustomer.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //.andExpect(jsonPath("$.firstname", Matchers.is("Kazimiera")));
        //then
        CustomerDTO customer = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerDTO.class);
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstname()).isEqualTo("Vidkun");
        assertThat(customer.getCity()).isEqualTo("Oslo");
    }

    @Test
    @WithMockUser(username = "ofiabrzydal@agiklocrm.comz", password = "123", authorities = "EMPLOYEE")
    void shouldNoGetCustomerById() throws Exception {
        //given
        List<Customers> customers = customerRepository.findAll();
        long fakeId;
        if (customers.isEmpty()) {
            fakeId = 1;}
        else {
            fakeId = customers.stream()
                    .mapToLong(Customers::getId)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
        }
        fakeId++;
        //when
        mockMvc.perform(get("/api/v1/customers/" + fakeId)).andDo(print())
        //then
        .andExpect(status().is(404))
        .andReturn();
    }

    @Test
    @Transactional
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldGetCustomersByFirstname() throws Exception {
        //given
        Customers fakeCustomer = prepareCustomerToTest();
        customerRepository.save(fakeCustomer);
        //when
        mockMvc.perform(get("/api/v1/customers/?firstname=" + fakeCustomer.getFirstname()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    Customers prepareCustomerToTest(){
        Customers newCustomer = new Customers();
        newCustomer.setFirstname("Vidkun");
        newCustomer.setLastname("Rikardson");
        newCustomer.setPesel("97542899482");
        newCustomer.setCity("Oslo");
        newCustomer.setZipCode("300-20");
        return newCustomer;
    }
}
