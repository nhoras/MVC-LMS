package ru.mts.teta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.multipart.MultipartFile;
import ru.mts.teta.domain.Role;
import ru.mts.teta.dto.CourseDto;
import ru.mts.teta.dto.UserDto;
import ru.mts.teta.exception.ImageFoundException;
import ru.mts.teta.exception.InternalServerError;
import ru.mts.teta.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserService userService;
    private final CourseCoverService coverService;
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    public CourseController(CourseService courseService, LessonService lessonService,
                            UserService userService, CourseCoverService coverService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userService = userService;
        this.coverService = coverService;
    }

    @GetMapping
    public String courseTable(Model model,
                              @RequestParam(name = "titlePrefix", required = false, defaultValue = "") String titlePrefix) {
        model.addAttribute("activePage", "courses");
        model.addAttribute("courses", courseService.findByTitleLike(titlePrefix));
        return "course_table";
    }

    @Secured(Role.ADMIN)
    @PostMapping
    public String submitCourseForm(@Valid @ModelAttribute("course") CourseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "course_form";
        }
        courseService.save(dto);
        return "redirect:/course";
    }

    @Secured(Role.ADMIN)
    @GetMapping("/new")
    public String courseForm(Model model) {
        model.addAttribute("activePage", "courses");
        model.addAttribute("course", courseService.createNewCourse());
        return "course_form";
    }

    @Secured(Role.ADMIN)
    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteById(id);
        return "redirect:/course";
    }

    @GetMapping("/{id}")
    public String courseForm(Model model, HttpServletRequest request, @PathVariable("id") Long courseId) {
        model.addAttribute("activePage", "courses");
        model.addAttribute("course", courseService.findById(courseId));
        List<UserDto> usersAssignedToCourse = userService.findUsersAssignedToCourse(courseId);
        if (request.isUserInRole(Role.ADMIN)) {
            model.addAttribute("users", usersAssignedToCourse);
            model.addAttribute("lessons", lessonService.findAllLessonsByCourseIdWithoutText(courseId));
        } else {
            UserDto currentUser = userService.findUserByUsername(request.getRemoteUser());
            if (usersAssignedToCourse.contains(currentUser)) {
                model.addAttribute("users", List.of(currentUser));
                model.addAttribute("lessons", lessonService.findAllLessonsByCourseIdWithoutText(courseId));
            } else {
                model.addAttribute("users", List.of());
                model.addAttribute("lessons", List.of());
            }
        }
        return "course_form";
    }

    @GetMapping("/{id}/assign")
    public String assignCourse(Model model, HttpServletRequest request, @PathVariable("id") Long courseId) {
        model.addAttribute("courseId",courseId);
        List<UserDto> usersNotAssignedToCourse = userService.findUsersNotAssignedToCourse(courseId);
        if (request.isUserInRole(Role.ADMIN)) {
            model.addAttribute("users", usersNotAssignedToCourse);
        } else {
            UserDto currentUser = userService.findUserByUsername(request.getRemoteUser());
            if (usersNotAssignedToCourse.contains(currentUser)) {
                model.addAttribute("users", List.of(currentUser));
            } else {
                model.addAttribute("users", List.of());
            }
        }
        return "course_assign_form";
    }

    @PostMapping("/{id}/assign")
    public String assignUserToCourse(@PathVariable("id") Long courseId, @RequestParam("userId") Long userId,
                                     HttpServletRequest request) {
        if (request.isUserInRole(Role.ADMIN) ||
                userId.equals(userService.findUserByUsername(request.getRemoteUser()).getId())) {
            courseService.assignUserToCourse(courseId, userId);
        }
        return String.format("redirect:/course/%d", courseId);
    }

    @PostMapping("/{courseId}/unassign")
    public String unassignUserFromCourse(@PathVariable("courseId") Long courseId, @RequestParam("userId") Long userId,
                                         HttpServletRequest request) {
        if (request.isUserInRole(Role.ADMIN) ||
                userId.equals(userService.findUserByUsername(request.getRemoteUser()).getId())) {
            courseService.unassignUserFromCourse(courseId, userId);
        }
        return String.format("redirect:/course/%d",courseId);
    }

    @GetMapping("/cover/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> avatarImage(@PathVariable("id") Long courseId) {
        String contentType = coverService.getContentTypeByCourseId(courseId)
                .orElseThrow(ImageFoundException::new);
        byte[] data = coverService.getCoverImageByCourseId(courseId)
                .orElseThrow(ImageFoundException::new);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }

    @PostMapping("/cover/{id}")
    public String updateCoverImage(@PathVariable("id") Long courseId, @RequestParam("cover") MultipartFile cover) {
        logger.info("File name {}, file content type {}, file size {}",
                cover.getOriginalFilename(), cover.getContentType(), cover.getSize());
        try {
            coverService.save(courseId, cover.getContentType(), cover.getInputStream());
        } catch (Exception ex) {
            logger.info("", ex);
            throw new InternalServerError();
        }
        return String.format("redirect:/course/%d",courseId);
    }
}
