package ru.mts.teta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mts.teta.domain.Role;
import ru.mts.teta.dto.UserDto;
import ru.mts.teta.exception.ImageFoundException;
import ru.mts.teta.exception.InternalServerError;
import ru.mts.teta.service.AvatarStorageService;
import ru.mts.teta.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final AvatarStorageService avatarStorageService;
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    public ProfileController(UserService userService, AvatarStorageService avatarStorageService) {
        this.userService = userService;
        this.avatarStorageService = avatarStorageService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String profileForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", userService.findUserByUsername(request.getRemoteUser()));
        return "profile_form";
    }

    @ModelAttribute("roles")
    public Set<Role> rolesAttribute(HttpServletRequest request) {
        return userService.findUserByUsername(request.getRemoteUser()).getRoles();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public String submitProfileForm(@Valid @ModelAttribute("user") UserDto dto, BindingResult bindingResult,
                                    HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "profile_form";
        }
        dto.setRoles(userService.findUserByUsername(request.getRemoteUser()).getRoles());
        userService.save(dto);
        return "redirect:/";
    }

    @PostMapping("/avatar")
    public String updateAvatarImage(Authentication auth, @RequestParam("avatar") MultipartFile avatar) {
        logger.info("File name {}, file content type {}, file size {}",
                avatar.getOriginalFilename(), avatar.getContentType(), avatar.getSize());
        try {
            avatarStorageService.save(auth.getName(), avatar.getContentType(), avatar.getInputStream());
        } catch (Exception ex) {
            logger.info("", ex);
            throw new InternalServerError();
        }
        return "redirect:/profile";
    }

    @GetMapping("/avatar")
    @ResponseBody
    public ResponseEntity<byte[]> avatarImage(Authentication auth) {
        String contentType = avatarStorageService.getContentTypeByUsername(auth.getName())
                .orElseThrow(ImageFoundException::new);
        byte[] data = avatarStorageService.getAvatarImageByUsername(auth.getName())
                .orElseThrow(ImageFoundException::new);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }
}
