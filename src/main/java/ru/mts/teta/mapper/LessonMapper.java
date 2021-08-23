package ru.mts.teta.mapper;

import org.springframework.stereotype.Service;
import ru.mts.teta.domain.Course;
import ru.mts.teta.domain.Lesson;
import ru.mts.teta.dto.LessonDto;

@Service
public class LessonMapper {

    public LessonDto mapToDto(Lesson lesson) {
        return new LessonDto(lesson.getId(), lesson.getTitle(), lesson.getText(), lesson.getCourse().getId());
    }

    public Lesson mapToLesson(LessonDto dto, Course course) {
        return new Lesson(dto.getId(), dto.getTitle(), dto.getText(), course);
    }
}
