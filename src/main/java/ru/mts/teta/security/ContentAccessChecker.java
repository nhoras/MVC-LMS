package ru.mts.teta.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mts.teta.domain.Role;
import ru.mts.teta.dto.LessonDto;
import ru.mts.teta.service.LessonService;
import ru.mts.teta.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Component("ContentAccessChecker")
class ContentAccessChecker {

    private final LessonService lessonService;
    private final UserService userService;

    @Autowired
    public ContentAccessChecker(LessonService lessonService, UserService userService) {
        this.lessonService = lessonService;
        this.userService = userService;
    }

    public boolean hasAccessToLesson(HttpServletRequest request, Long lessonId) {
        LessonDto lessonDto = lessonService.findLessonDtoById(lessonId);
        return request.isUserInRole(Role.ADMIN) ||
                userService.findUsersAssignedToCourse(lessonDto.getCourseId())
                        .contains(userService.findUserByUsername(request.getRemoteUser()));
    }
}
