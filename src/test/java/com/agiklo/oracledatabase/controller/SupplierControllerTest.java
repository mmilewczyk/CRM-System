package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Supplier;
import com.agiklo.oracledatabase.entity.dto.SupplierDTO;
import com.agiklo.oracledatabase.repository.SupplierRepository;
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
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void shouldNoGetSuppliersAndReturnForbridden() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/suppliers"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Access Denied");
    }

    @Test
    @Transactional
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldGetSupplierById() throws Exception {
        //given
        Supplier fakeSupplier = prepareSupplierToTest();
        supplierRepository.save(fakeSupplier);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/suppliers/" + fakeSupplier.getSupplierId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        SupplierDTO supplier = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SupplierDTO.class);
        assertThat(supplier).isNotNull();
        assertThat(supplier.getSupplierName()).isEqualTo("mitopharma");
        assertThat(supplier.getActivityStatus()).isEqualTo("A");
    }

    @Test
    @WithMockUser(username = "ofiabrzydal@agiklocrm.comz", password = "123", authorities = "EMPLOYEE")
    void shouldNoGetSupplierById() throws Exception {
        //given
        List<Supplier> suppliers = supplierRepository.findAll();
        long fakeId;
        if (suppliers.isEmpty()) {
            fakeId = 1;}
        else {
            fakeId = suppliers.stream()
                    .mapToLong(Supplier::getSupplierId)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
        }
        fakeId++;
        //when
        mockMvc.perform(get("/api/v1/suppliers/" + fakeId)).andDo(print())
                //then
                .andExpect(status().is(404))
                .andReturn();
    }

    Supplier prepareSupplierToTest(){
        Supplier newSupplier = new Supplier();
        newSupplier.setSupplierName("mitopharma");
        newSupplier.setActivityStatus("A");
        return newSupplier;
    }
}
