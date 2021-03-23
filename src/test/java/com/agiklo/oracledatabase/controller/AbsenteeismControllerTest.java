package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.*;
import com.agiklo.oracledatabase.repository.AbsenteeismRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
class AbsenteeismControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AbsenteeismRepository absenteeismRepository;

    @Test
    void shouldNoGetAbsenteeismAndReturnForbridden() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/absenteeisms"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Access Denied");
    }

//    @Test
//    @Transactional
//    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
//    void shouldGetAbsenteeismById() throws Exception {
//        //given
//        Absenteeism fakeAbsenteeism = prepareAbsenteeismToTest();
//        absenteeismRepository.save(fakeAbsenteeism);
//        //when
//        MvcResult mvcResult = mockMvc.perform(get("/api/v1/absenteeisms/" + fakeAbsenteeism.getId()))
//                .andDo(print())
//                .andExpect(status().is2xxSuccessful())
//                .andReturn();
//        //then
//        AbsenteeismDTO absenteeism = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AbsenteeismDTO.class);
//        assertThat(absenteeism).isNotNull();
//        assertThat(absenteeism.getAbsenteeismName()).isEqualTo("Macierzyński");
//    }

    @Test
    @WithMockUser(username = "zofiabrzydal@agiklocrm.com", password = "123", authorities = "EMPLOYEE")
    void shouldNoGetAbsenteeismById() throws Exception {
        //given
        List<Absenteeism> absenteeisms = absenteeismRepository.findAll();
        long fakeId;
        if (absenteeisms.isEmpty()) {
            fakeId = 1;}
        else {
            fakeId = absenteeisms.stream()
                    .mapToLong(Absenteeism::getId)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
        }
        fakeId++;
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/absenteeisms/" + fakeId))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
        //then
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo("Absenteeism cannot be found, the specified id does not exist");
    }

    Absenteeism prepareAbsenteeismToTest(){
        Absenteeism absenteeism = new Absenteeism();
        absenteeism.setId(1L);
        absenteeism.setEmployee(new Employee());
        absenteeism.setReasonOfAbsenteeismCode(new ReasonsOfAbsenteeism("Macierzyński", 'A', null));
        return absenteeism;
    }

}
