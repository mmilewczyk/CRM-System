package com.agiklo.oracledatabase.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Execution(ExecutionMode.CONCURRENT)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldLogin() throws Exception {
        mockMvc.perform(post("/login")
                .content("{\"username\": \"zofiabrzydal@agiklocrm.com\", \"password\": \"123\"}"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void shouldNoLogin() throws Exception {
        mockMvc.perform(post("/login")
                .content("{\"username\": \"fakeuser@test.com\", \"password\": \"test\"}"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

}
