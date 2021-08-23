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
import ru.mts.teta.domain.Role;
import ru.mts.teta.dto.UserDto;
import ru.mts.teta.service.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userServiceMock;
    @MockBean
    private RoleService roleServiceMock;

    private static final Role ROLE_STUDENT = new Role(1L, Role.STUDENT);
    private static final UserDto USER1DTO = new UserDto(1L, "user1Name", Set.of(ROLE_STUDENT));
    private static final UserDto USER_TO_SAVE = new UserDto(1L, "user1Name", "password", Set.of(ROLE_STUDENT));

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void userTable() throws Exception {
        ModelAndView mav = mockMvc
                .perform(get("/admin/user"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "user_table");
    }

    @Test
    @WithMockUser(username="user",roles={"STUDENT"})
    void userTableForNoAdmin() throws Exception {
        mockMvc
                .perform(get("/admin/user"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/access_denied"));
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void newUserForm() throws Exception {
        Mockito.when(userServiceMock.createNewUser()).thenReturn(new UserDto());
        ModelAndView mav = mockMvc
                .perform(get("/admin/user/new"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "user_form");
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void userForm() throws Exception {
        Mockito.when(userServiceMock.findById(1L)).thenReturn(USER1DTO);
        ModelAndView mav = mockMvc
                .perform(get("/admin/user/{id}", USER1DTO.getId()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "user_form");
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void submitUserCreateForm() throws Exception {
        ModelAndView mav = mockMvc
                .perform(post("/admin/user").flashAttr("user", USER_TO_SAVE).with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user"))
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "redirect:/admin/user");

        mav = mockMvc
                .perform(post("/admin/user").flashAttr("course", new UserDto()).with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(status().isOk())
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "user_form");
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    void deleteUser() throws Exception {
        ModelAndView mav = mockMvc
                .perform(delete("/admin/user/{id}", 1L).with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user"))
                .andReturn().getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "redirect:/admin/user");
    }

    @Test
    void testAnonymousUser() throws Exception {
        mockMvc
                .perform(get("/admin/user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        mockMvc
                .perform(get("/admin/user/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        mockMvc
                .perform(get("/admin/user/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}