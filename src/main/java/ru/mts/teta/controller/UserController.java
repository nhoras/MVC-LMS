package ru.mts.teta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mts.teta.domain.Role;
import ru.mts.teta.dto.UserDto;
import ru.mts.teta.service.RoleService;
import ru.mts.teta.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@Secured(Role.ADMIN)
@RequestMapping("/admin/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String userTable(Model model,
                            @RequestParam(name = "usernamePrefix", required = false, defaultValue = "") String usernamePrefix) {
        model.addAttribute("activePage", "users");
        model.addAttribute("users", userService.findByUsernameLike(usernamePrefix));
        return "user_table";
    }

    @GetMapping("/new")
    public String UserForm(Model model) {
        model.addAttribute("activePage", "user");
        model.addAttribute("user", userService.createNewUser());
        return "user_form";
    }

    @GetMapping("/{id}")
    public String userForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("activePage", "users");
        model.addAttribute("user", userService.findById(id));
        return "user_form";
    }

    @PostMapping
    public String submitUserCreateForm(@Valid @ModelAttribute("user") UserDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user_form";
        }
        userService.save(dto);
        return "redirect:/admin/user";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/user";
    }

    @ModelAttribute("roles")
    public List<Role> rolesAttribute() {
        return roleService.findAll();
    }
}
