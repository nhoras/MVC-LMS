package ru.mts.teta.mapper;

import org.springframework.stereotype.Service;
import ru.mts.teta.domain.Course;
import ru.mts.teta.dto.CourseDto;

@Service
public class CourseMapper {

    public CourseDto mapToDto(Course course) {
        return new CourseDto(course.getId(), course.getAuthor(), course.getTitle());
    }

    public Course mapToCourse(CourseDto dto) {
        return new Course(dto.getId(), dto.getAuthor(), dto.getTitle());
    }
}
