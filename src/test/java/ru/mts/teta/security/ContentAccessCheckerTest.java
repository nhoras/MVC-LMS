package ru.mts.teta.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.mts.teta.domain.Course;
import ru.mts.teta.domain.Role;
import ru.mts.teta.dto.LessonDto;
import ru.mts.teta.dto.UserDto;
import ru.mts.teta.service.LessonService;
import ru.mts.teta.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ContentAccessCheckerTest {

    private static ContentAccessChecker accessChecker;
    private static final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    private static final Role ROLE_STUDENT = new Role(1L, Role.STUDENT);
    private static final UserDto USER1DTO_STUDENT = new UserDto(1L, "Student1", "password", Set.of(ROLE_STUDENT));
    private static final UserDto USER2DTO_STUDENT = new UserDto(2L, "Student2", "password", Set.of(ROLE_STUDENT));
    private static final UserDto USER3DTO_STUDENT = new UserDto(3L, "Student3", "password", Set.of(ROLE_STUDENT));
    private static final UserDto USER_NO_ROLES = new UserDto(4L, "Stranger", "password", Set.of());
    private static final List<UserDto> LIST_USERS_ASSIGNED_TO_COURSE = List.of(USER1DTO_STUDENT, USER2DTO_STUDENT);
    private static final Course COURSE1 = new Course(1L, "Author1", "Course1");
    private static final LessonDto LESSON1DTO = new LessonDto(1L, "Lesson1Title", "Lesson1Text", COURSE1.getId());
    private static final LessonDto LESSON2DTO = new LessonDto(2L, "Lesson2Title", "Lesson2Text", COURSE1.getId());
    private static final LessonDto LESSON3DTO = new LessonDto(3L, "Lesson3Title", "Lesson3Text", COURSE1.getId());

    @BeforeAll
    static void setUp() {
        LessonService lessonServiceMock = Mockito.mock(LessonService.class);
        UserService userServiceMock = Mockito.mock(UserService.class);

        Mockito.when(lessonServiceMock.findLessonDtoById(1L)).thenReturn(LESSON1DTO);
        Mockito.when(lessonServiceMock.findLessonDtoById(2L)).thenReturn(LESSON2DTO);
        Mockito.when(lessonServiceMock.findLessonDtoById(3L)).thenReturn(LESSON3DTO);
        Mockito.when(userServiceMock.findUsersAssignedToCourse(1L)).thenReturn(LIST_USERS_ASSIGNED_TO_COURSE);
        Mockito.when(userServiceMock.findUserByUsername("Student1")).thenReturn(USER1DTO_STUDENT);
        Mockito.when(userServiceMock.findUserByUsername("Student2")).thenReturn(USER2DTO_STUDENT);
        Mockito.when(userServiceMock.findUserByUsername("Student3")).thenReturn(USER3DTO_STUDENT);
        Mockito.when(userServiceMock.findUserByUsername("Stranger")).thenReturn(USER_NO_ROLES);

        accessChecker = new ContentAccessChecker(lessonServiceMock, userServiceMock);
    }

    @Test
    void adminAccess() {
        Mockito.when(request.isUserInRole(Role.ADMIN)).thenReturn(true);

        assertTrue(accessChecker.hasAccessToLesson(request, 1L), "Admin must have access to all lessons");
        assertTrue(accessChecker.hasAccessToLesson(request, 2L), "Admin must have access to all lessons");
        assertTrue(accessChecker.hasAccessToLesson(request, 3L), "Admin must have access to all lessons");
    }

    @Test
    void userWithoutRoles() {
        Mockito.when(request.isUserInRole(Role.ADMIN)).thenReturn(false);
        Mockito.when(request.getRemoteUser()).thenReturn(USER_NO_ROLES.getUsername());

        assertFalse(accessChecker.hasAccessToLesson(request, 1L),
                "User without roles can't have access to any lessons");
        assertFalse(accessChecker.hasAccessToLesson(request, 2L),
                "User without roles can't have access to any lessons");
        assertFalse(accessChecker.hasAccessToLesson(request, 3L),
                "User without roles can't have access to any lessons");
    }

    @Test
    void userNotAssigned() {
        Mockito.when(request.isUserInRole(Role.ADMIN)).thenReturn(false);
        Mockito.when(request.getRemoteUser()).thenReturn(USER3DTO_STUDENT.getUsername());

        assertFalse(accessChecker.hasAccessToLesson(request, 1L),
                "This user can't have access to this lessons because he not assigned to course");
        assertFalse(accessChecker.hasAccessToLesson(request, 2L),
                "This user can't have access to this lessons because he not assigned to course");
        assertFalse(accessChecker.hasAccessToLesson(request, 3L),
                "This user can't have access to this lessons because he not assigned to course");
    }

    @Test
    void userAssigned() {
        Mockito.when(request.isUserInRole(Role.ADMIN)).thenReturn(false);
        Mockito.when(request.getRemoteUser()).thenReturn(USER1DTO_STUDENT.getUsername());

        assertTrue(accessChecker.hasAccessToLesson(request, 1L),
                "This user must have access to lesson because he assigned to course");
        assertTrue(accessChecker.hasAccessToLesson(request, 2L),
                "This user must have access to lesson because he assigned to course");
        assertTrue(accessChecker.hasAccessToLesson(request, 3L),
                "This user must have access to lesson because he assigned to course");
    }
}