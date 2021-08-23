package ru.mts.teta.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.mts.teta.dao.CourseRepository;
import ru.mts.teta.dao.LessonRepository;
import ru.mts.teta.domain.Course;
import ru.mts.teta.domain.Lesson;
import ru.mts.teta.dto.LessonDto;
import ru.mts.teta.exception.NotFoundException;
import ru.mts.teta.mapper.LessonMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LessonServiceImplTest {

    private static LessonServiceImpl lessonService;

    private static final Course COURSE1 = new Course(1L, "Author1", "Course1");
    private static final Course COURSE2 = new Course(2L, "Author2", "Course2");

    private static final Lesson LESSON1 = new Lesson(1L, "Lesson1Title", "Lesson1Text", COURSE1);
    private static final Lesson LESSON2 = new Lesson(2L, "Lesson2Title", "Lesson2Text", COURSE2);

    private static final LessonDto LESSON1DTO = new LessonDto(1L, "Lesson1Title", "Lesson1Text", COURSE1.getId());
    private static final LessonDto LESSON2DTO = new LessonDto(2L, "Lesson2Title", "Lesson2Text", COURSE2.getId());

    private static final Long IMPOSSIBLE_LESSON_ID = 2345676543247L;

    @BeforeAll
    static void setUp() {
        LessonRepository lessonRepositoryMock = Mockito.mock(LessonRepository.class);
        CourseRepository courseRepositoryMock = Mockito.mock(CourseRepository.class);

        Mockito.when(lessonRepositoryMock.findById(1L)).thenReturn(Optional.of(LESSON1));
        Mockito.when(lessonRepositoryMock.findById(2L)).thenReturn(Optional.of(LESSON2));

        LessonMapper mapper = new LessonMapper();

        lessonService = new LessonServiceImpl(lessonRepositoryMock, courseRepositoryMock, mapper);
    }

    @Test
    void findLessonDtoById() {
        assertEquals(LESSON1DTO, lessonService.findLessonDtoById(1L), "there isn't expected LessonDTO");
        assertEquals(LESSON2DTO, lessonService.findLessonDtoById(2L), "there isn't expected LessonDTO");
        assertThrows(NotFoundException.class, () -> lessonService.findLessonDtoById(IMPOSSIBLE_LESSON_ID),
                "Impossible lesson id doesn't throw NotFoundException");
        assertThrows(NotFoundException.class, () -> lessonService.findLessonDtoById(null),
                "Null lesson id doesn't throw NotFoundException");
    }

    @Test
    void createLesson() {
        assertEquals(new LessonDto(COURSE1.getId()), lessonService.createLesson(COURSE1.getId()),
                "there isn't expected LessonDTO");
        assertEquals(new LessonDto(COURSE2.getId()), lessonService.createLesson(COURSE2.getId()),
                "there isn't expected LessonDTO");
    }
}
