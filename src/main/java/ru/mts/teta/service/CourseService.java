package ru.mts.teta.service;

import ru.mts.teta.dto.CourseDto;

import java.util.List;


public interface CourseService {

    CourseDto createNewCourse();

    void deleteById(Long id);

    List<CourseDto> findByTitleLike(String title);

    void assignUserToCourse(Long courseId, Long userId);

    void unassignUserFromCourse(Long courseId, Long userId);

    void save(CourseDto dto);

    CourseDto findById(Long id);
}
