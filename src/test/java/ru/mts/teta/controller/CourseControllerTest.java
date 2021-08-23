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
import ru.mts.teta.dto.CourseDto;
import ru.mts.teta.service.CourseService;
import ru.mts.teta.service.LessonService;
import ru.mts.teta.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseServiceMock;
    @MockBean
    private LessonService lessonServiceMock;
    @MockBean
    private UserService userServiceMock;

    private static final CourseDto COURSE1DTO = new CourseDto(1L, "Author", "Course");
    private static final CourseDto COURSE_DTO_BLANK = new CourseDto(null, "         ", "      ");
    private static final CourseDto COURSE_DTO_NEW = new CourseDto();

    @Test
    @WithMockUser
    void courseTable() throws Exception {
        ModelAndView mav = mockMvc
                .perform(get("/course"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "course_table");
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void submitCourseFormByAdmin() throws Exception {
        ModelAndView mav = mockMvc
                .perform(post("/course").flashAttr("course", COURSE1DTO).with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"))
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "redirect:/course");

        mav = mockMvc
                .perform(post("/course").flashAttr("course", COURSE_DTO_BLANK).with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "course_form");

        mav = mockMvc
                .perform(post("/course").flashAttr("course", COURSE_DTO_NEW).with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "course_form");
    }

    @Test
    @WithMockUser(username="student",roles={"STUDENT"})
    void submitCourseFormByStudent() throws Exception {
        mockMvc
                .perform(post("/course").flashAttr("course", COURSE1DTO))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/access_denied"));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void newCourseForm() throws Exception {
        Mockito.when(courseServiceMock.createNewCourse()).thenReturn(new CourseDto());
        ModelAndView mav = mockMvc
                .perform(get("/course/new"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "course_form");
    }

    @Test
    @WithMockUser(username="student",roles={"STUDENT"})
    void courseForm() throws Exception {
        Mockito.when(courseServiceMock.findById(1L)).thenReturn(COURSE1DTO);
        ModelAndView mav = mockMvc
                .perform(get("/course/{id}", 1L))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "course_form");
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void deleteCourse() throws Exception {
        ModelAndView mav = mockMvc
                .perform(delete("/course/{id}", 1L).with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course"))
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "redirect:/course");
    }

    @Test
    @WithMockUser(username="student",roles={"STUDENT"})
    void deleteCourseByStudent() throws Exception {
        mockMvc
                .perform(delete("/course/{id}", 1L))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/access_denied"));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void assignCourse() throws Exception {
        Mockito.when(courseServiceMock.findById(1L)).thenReturn(COURSE1DTO);
        ModelAndView mav = mockMvc
                .perform(get("/course/{id}/assign", 1L))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "course_assign_form");
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void assignUserToCourse() throws Exception {
        ModelAndView mav = mockMvc
                .perform(post("/course/{id}/assign", 1L).param("userId", "1").with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/course/1"))
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "redirect:/course/1");
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void unassignUserFromCourse() throws Exception {
        ModelAndView mav = mockMvc
                .perform(post("/course/{id}/unassign", 1L).param("userId", "1").with(csrf()))
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
                .perform(get("/course"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        mockMvc
                .perform(get("/course/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        mockMvc
                .perform(get("/course/{id}/assign", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        mockMvc
                .perform(get("/course/{id}/unassign", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        mockMvc
                .perform(get("/course/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}