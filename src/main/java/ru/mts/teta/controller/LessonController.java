package ru.mts.teta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mts.teta.domain.Role;
import ru.mts.teta.dto.LessonDto;
import ru.mts.teta.exception.NotFoundException;
import ru.mts.teta.service.LessonService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/lesson")
public class LessonController {

    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Secured(Role.ADMIN)
    @GetMapping("/new")
    public String createLesson(Model model, @RequestParam("course_id") Long courseId) {
        model.addAttribute("lesson", lessonService.createLesson(courseId));
        return "lesson_form";
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ContentAccessChecker.hasAccessToLesson(#request, #id)")
    public String lessonForm(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
        model.addAttribute("lesson", lessonService.findLessonDtoById(id));
        return "lesson_form";
    }

    @Secured(Role.ADMIN)
    @PostMapping
    public String submitLessonForm(@Valid @ModelAttribute("lesson") LessonDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "lesson_form";
        }
        lessonService.saveLesson(dto);
        return String.format("redirect:/course/%d",dto.getCourseId());
    }

    @Secured(Role.ADMIN)
    @DeleteMapping("/{id}")
    public String deleteLesson(@PathVariable("id") Long id) {
        long courseId = lessonService.findById(id).orElseThrow(NotFoundException::new).getCourse().getId();
        lessonService.deleteByID(id);
        return String.format("redirect:/course/%d", courseId);
    }
}
