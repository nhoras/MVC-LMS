package ru.mts.teta.service;

import org.springframework.data.repository.query.Param;
import ru.mts.teta.domain.Lesson;
import ru.mts.teta.dto.LessonDto;

import java.util.List;
import java.util.Optional;

public interface LessonService {

    LessonDto findLessonDtoById(Long id);

    Optional<Lesson> findById(Long id);

    void deleteByID(Long id);

    List<LessonDto> findAllLessonsByCourseIdWithoutText(@Param("id") Long id);

    LessonDto createLesson(Long courseId);

    Lesson saveLesson(LessonDto dto);
}
