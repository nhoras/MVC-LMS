package ru.mts.teta.service;

import ru.mts.teta.dao.CourseRepository;
import ru.mts.teta.dao.UserRepository;
import ru.mts.teta.domain.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.teta.domain.User;
import ru.mts.teta.dto.CourseDto;
import ru.mts.teta.exception.NotFoundException;
import ru.mts.teta.mapper.CourseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(UserRepository userRepository, CourseRepository courseRepository, CourseMapper courseMapper) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public CourseDto createNewCourse() {
        return new CourseDto();
    }

    @Override
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public CourseDto findById(Long id) {
        return courseRepository.findById(id).map(courseMapper::mapToDto).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<CourseDto> findByTitleLike(String title) {
        return courseRepository.findByTitleLike(title + "%").stream().map(courseMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void assignUserToCourse(Long courseId, Long userId) {
        User user = userRepository.getById(userId);
        Course course = courseRepository.getById(courseId);
        course.getUsers().add(user);
        user.getCourses().add(course);
        courseRepository.save(course);
    }

    @Override
    public void unassignUserFromCourse(Long courseId, Long userId) {
        User user = userRepository.getById(userId);
        Course course = courseRepository.getById(courseId);
        course.getUsers().remove(user);
        user.getCourses().remove(course);
        courseRepository.save(course);
    }

    @Override
    public void save(CourseDto dto) {
        Course course = courseMapper.mapToCourse(dto);
        if (course.getId() != null) {
            course.setUsers(userRepository.findUsersAssignedToCourse(course.getId()));
        }
        courseRepository.save(course);
    }

}
