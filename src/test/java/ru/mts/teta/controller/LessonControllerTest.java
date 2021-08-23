package ru.mts.teta.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;
import ru.mts.teta.domain.Course;
import ru.mts.teta.domain.Lesson;
import ru.mts.teta.dto.LessonDto;
import ru.mts.teta.service.LessonService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonServiceMock;

    private static final Course COURSE1 = new Course(1L, "Author1", "Course1");
    private static final Lesson LESSON1 = new Lesson(1L, "Lesson1Title", "Lesson1Text", COURSE1);
    private static final LessonDto LESSON1DTO = new LessonDto(1L, "Lesson1Title", "Lesson1Text", 1L);
    private static final LessonDto LESSON_DTO_BLANK = new LessonDto(null, "         ", "        ", 2L);
    private static final LessonDto LESSON_DTO_NEW = new LessonDto( 2L);


    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void createLesson() throws Exception {
        Mockito.when(lessonServiceMock.createLesson(1L)).thenReturn(LESSON_DTO_NEW);
        ModelAndView mav = mockMvc
                .perform(get("/lesson/new").param("course_id", "1"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "lesson_form");
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void lessonForm() throws Exception {
        Mockito.when(lessonServiceMock.findLessonDtoById(1L)).thenReturn(LESSON1DTO);
        ModelAndView mav = mockMvc
                .perform(get("/lesson/{id}", 1L))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "lesson_form");
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void submitLessonForm() throws Exception {
        ModelAndView mav = mockMvc
                .perform(post("/lesson").flashAttr("lesson", LESSON1DTO).with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(String.format("/course/%d",LESSON1DTO.getCourseId())))
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, String.format("redirect:/course/%d",LESSON1DTO.getCourseId()));

        mav = mockMvc
                .perform(post("/lesson").flashAttr("lesson", LESSON_DTO_BLANK).with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "lesson_form");

        mav = mockMvc
                .perform(post("/lesson").flashAttr("lesson", LESSON_DTO_NEW).with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "lesson_form");
    }

    @Test
    @WithMockUser(username="student",roles={"STUDENT"})
    void submitLessonFormByStudent() throws Exception {
        mockMvc
                .perform(post("/lesson").flashAttr("lesson", LESSON1DTO))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/access_denied"));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void deleteLesson() throws Exception {
        Mockito.when(lessonServiceMock.findById(1L)).thenReturn(Optional.of(LESSON1));
        ModelAndView mav = mockMvc
                .perform(delete("/lesson/{id}", 1L).with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course/1"))
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "redirect:/course/1");
    }

    @Test
    void testAnonymousUser() throws Exception {
        mockMvc
                .perform(get("/lesson"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        mockMvc
                .perform(get("/lesson/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        mockMvc
                .perform(get("/lesson/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}