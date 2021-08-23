package ru.mts.teta.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mts.teta.dao.CourseRepository;
import ru.mts.teta.dao.UserRepository;
import ru.mts.teta.domain.Role;
import ru.mts.teta.domain.User;
import ru.mts.teta.dto.UserDto;
import ru.mts.teta.exception.NotFoundException;
import ru.mts.teta.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private static UserServiceImpl userService;

    private static final Role ROLE_STUDENT = new Role(1L, Role.STUDENT);
    private static final User USER1 = new User(1L, "user1Name", "password", Set.of(ROLE_STUDENT));
    private static final User USER2 = new User(2L, "user2Name", "password", Set.of(ROLE_STUDENT));
    private static final User USER3 = new User(3L, "user3Name", "password", Set.of(ROLE_STUDENT));
    private static final User USER4 = new User(4L, "user4Name", "password", Set.of(ROLE_STUDENT));
    private static final Set<User> SET_USERS_NOT_ASSIGNED_TO_COURSE = Set.of(USER1, USER3);
    private static final Set<User> SET_USERS_ASSIGNED_TO_COURSE = Set.of(USER2, USER4);

    private static final UserDto USER1DTO = new UserDto(1L, "user1Name", Set.of(ROLE_STUDENT));
    private static final UserDto USER2DTO = new UserDto(2L, "user2Name", Set.of(ROLE_STUDENT));
    private static final UserDto USER3DTO = new UserDto(3L, "user3Name", Set.of(ROLE_STUDENT));
    private static final UserDto USER4DTO = new UserDto(4L, "user4Name", Set.of(ROLE_STUDENT));
    private static final List<UserDto> LIST_USERS_DTO_NOT_ASSIGNED_TO_COURSE = List.of(USER1DTO, USER3DTO);
    private static final List<UserDto> LIST_USERS_DTO_ASSIGNED_TO_COURSE = List.of(USER2DTO, USER4DTO);

    private static final Long IMPOSSIBLE_USER_ID = 2345676543247L;
    private static final String IMPOSSIBLE_USERNAME = "DFGFHFDSFG1513udwevbtfgjhhgd";

    @BeforeAll
    static void setUp() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        CourseRepository courseRepositoryMock = Mockito.mock(CourseRepository.class);
        PasswordEncoder passwordEncoderMock = Mockito.mock(PasswordEncoder.class);

        Mockito.when(passwordEncoderMock.encode("password")).thenReturn("cryptPassword");
        Mockito.when(userRepositoryMock.findUsersNotAssignedToCourse(1L)).thenReturn(SET_USERS_NOT_ASSIGNED_TO_COURSE);
        Mockito.when(userRepositoryMock.findUsersAssignedToCourse(2L)).thenReturn(SET_USERS_ASSIGNED_TO_COURSE);
        Mockito.when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(USER1));
        Mockito.when(userRepositoryMock.save(USER1)).thenReturn(USER1);
        Mockito.when(userRepositoryMock.findUserByUsername(USER1.getUsername())).thenReturn(Optional.of(USER1));

        UserMapper userMapper = new UserMapper(passwordEncoderMock);
        userService = new UserServiceImpl(userRepositoryMock, courseRepositoryMock, userMapper);
    }

    @Test
    void findUsersNotAssignedToCourse() {
        List<UserDto> actual = userService.findUsersNotAssignedToCourse(1L);
        assertEquals(LIST_USERS_DTO_NOT_ASSIGNED_TO_COURSE.size(), actual.size(),
                "expectedList size and actualList size not equal");
        assertTrue(actual.containsAll(LIST_USERS_DTO_NOT_ASSIGNED_TO_COURSE),
                "actual List doesn't contain all of expected UserDto");
    }

    @Test
    void findUsersAssignedToCourse() {
        List<UserDto> actual = userService.findUsersAssignedToCourse(2L);
        assertEquals(LIST_USERS_DTO_ASSIGNED_TO_COURSE.size(), actual.size(),
                "expectedList size and actualList size not equal");
        assertTrue(actual.containsAll(LIST_USERS_DTO_ASSIGNED_TO_COURSE),
                "actual List doesn't contain all of expected UserDto");
    }

    @Test
    void findById() {
        assertEquals(USER1DTO, userService.findById(1L),
                "there isn't expected UserDTO");
        assertThrows(NotFoundException.class, () -> userService.findById(IMPOSSIBLE_USER_ID),
                "Impossible user id doesn't throw NotFoundException");
        assertThrows(NotFoundException.class, () -> userService.findById(null),
                "Null user id doesn't throw NotFoundException");
    }

    @Test
    void save() {
        assertEquals(USER1, userService.save(USER1DTO), "save(UserDTO) must return User object");
    }

    @Test
    void createNewUser() {
        assertEquals(new UserDto(), userService.createNewUser());
    }

    @Test
    void findUserByUsername() {
        assertEquals(USER1DTO, userService.findUserByUsername(USER1.getUsername()),
                "there isn't expected UserDTO");
        assertThrows(NotFoundException.class, () -> userService.findUserByUsername(IMPOSSIBLE_USERNAME),
                "Impossible username doesn't throw NotFoundException");
        assertThrows(NotFoundException.class, () -> userService.findUserByUsername(null),
                "Null username doesn't throw NotFoundException");
    }
}