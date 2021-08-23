package ru.mts.teta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.teta.dao.CourseRepository;
import ru.mts.teta.exception.NotFoundException;
import ru.mts.teta.dao.LessonRepository;
import ru.mts.teta.domain.Course;
import ru.mts.teta.domain.Lesson;
import ru.mts.teta.dto.LessonDto;
import ru.mts.teta.mapper.LessonMapper;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper mapper;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, CourseRepository courseRepository, LessonMapper mapper) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.mapper = mapper;
    }

    @Override
    public LessonDto findLessonDtoById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.mapToDto(lesson);
    }

    @Override
    public Optional<Lesson> findById(Long id) {
        return lessonRepository.findById(id);
    }

    @Override
    public void deleteByID(Long id) {
        lessonRepository.deleteById(id);
    }

    public List<LessonDto> findAllLessonsByCourseIdWithoutText(Long courseId) {
        return lessonRepository.findAllLessonsByCourseIdWithoutText(courseId);
    }

    @Override
    public LessonDto createLesson(Long courseId) {
        return new LessonDto(courseId);
    }

    @Override
    public Lesson saveLesson(LessonDto dto) {
        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(NotFoundException::new);
        Lesson lesson = mapper.mapToLesson(dto, course);
        return lessonRepository.save(lesson);
    }

}
