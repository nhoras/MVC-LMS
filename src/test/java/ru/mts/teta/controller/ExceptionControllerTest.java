package ru.mts.teta.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExceptionController.class)
class ExceptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void notFoundStatusTest() throws Exception {
        mockMvc
                .perform(get("/course/0"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

        mockMvc
                .perform(get("/lesson/0"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

        mockMvc
                .perform(get("/admin/user/0"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }
}