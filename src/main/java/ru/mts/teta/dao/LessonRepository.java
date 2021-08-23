package ru.mts.teta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mts.teta.domain.Lesson;
import ru.mts.teta.dto.LessonDto;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("select new ru.mts.teta.dto.LessonDto(l.id, l.title, l.course.id) " +
            "from Lesson l where l.course.id = :id")
    List<LessonDto> findAllLessonsByCourseIdWithoutText(@Param("id") Long id);
}
