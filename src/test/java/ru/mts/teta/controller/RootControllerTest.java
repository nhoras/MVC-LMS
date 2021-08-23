package ru.mts.teta.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(RootController.class)
public class RootControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void mainPageTest() throws Exception {

        ModelAndView mav = mockMvc
                .perform(get("/"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "main_page");
    }

    @Test
    void accessDeniedPageTest() throws Exception {

        ModelAndView mav = mockMvc
                .perform(get("/access_denied"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "access_denied");
    }
}
