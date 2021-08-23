package ru.mts.teta.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.mts.teta.dao.CourseRepository;
import ru.mts.teta.dao.UserRepository;
import ru.mts.teta.domain.Course;
import ru.mts.teta.dto.CourseDto;
import ru.mts.teta.exception.NotFoundException;
import ru.mts.teta.mapper.CourseMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceImplTest {

    private static CourseServiceImpl courseService;

    private static final Course COURSE1 = new Course(1L, "Author1", "Course1");

    private static final CourseDto COURSE1DTO = new CourseDto(1L, "Author1", "Course1");

    private static final Long IMPOSSIBLE_COURSE_ID = 2345676543247L;

    @BeforeAll
    static void setUp() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        CourseRepository courseRepositoryMock = Mockito.mock(CourseRepository.class);

        Mockito.when(courseRepositoryMock.findById(1L)).thenReturn(Optional.of(COURSE1));

        CourseMapper mapper = new CourseMapper();

        courseService = new CourseServiceImpl(userRepositoryMock, courseRepositoryMock, mapper);
    }

    @Test
    void createNewCourse() {
        assertEquals(new CourseDto(), courseService.createNewCourse(), "there isn't expected CourseDTO");
    }

    @Test
    void findById() {
        assertEquals(COURSE1DTO, courseService.findById(1L), "there isn't expected CourseDTO");
        assertThrows(NotFoundException.class, () -> courseService.findById(IMPOSSIBLE_COURSE_ID),
                "Impossible course id doesn't throw NotFoundException");
        assertThrows(NotFoundException.class, () -> courseService.findById(null),
                "Null course id doesn't throw NotFoundException");
    }
}