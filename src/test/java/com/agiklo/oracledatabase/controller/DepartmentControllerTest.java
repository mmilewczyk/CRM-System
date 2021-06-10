package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.entity.dto.DepartmentDTO;
import com.agiklo.oracledatabase.repository.DepartmentsRepository;
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
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DepartmentsRepository departmentsRepository;

    @Test
    void shouldNoGetDepartmentsAndReturnForbridden() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/departments"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Access Denied");
    }

    @Test
    @Transactional
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldGetDepartmentById() throws Exception {
        //given
        Departments fakeDepartments = prepareDepartmentsToTest();
        departmentsRepository.save(fakeDepartments);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/departments/" + fakeDepartments.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        //then
        DepartmentDTO department = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), DepartmentDTO.class);
        assertThat(department).isNotNull();
        assertThat(department.getCity()).isEqualTo("Gdynia");
        assertThat(department.getDepartmentName()).isEqualTo("Sprzedaz");
    }

    @Test
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldNoGetDepartmentById() throws Exception {
        //given
        List<Departments> departments = departmentsRepository.findAll();
        long fakeId;
        if (departments.isEmpty()) {
            fakeId = 1;}
        else {
            fakeId = departments.stream()
                    .mapToLong(Departments::getId)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
        }
        fakeId++;
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/departments/" + fakeId))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Department cannot be found, the specified id does not exist");
    }

    Departments prepareDepartmentsToTest(){
        Departments departments = new Departments();
        departments.setCity("Gdynia");
        departments.setDepartmentName("Sprzedaz");
        return departments;
    }
}
