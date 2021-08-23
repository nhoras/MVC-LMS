package ru.mts.teta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.teta.dao.CourseRepository;
import ru.mts.teta.dao.UserRepository;
import ru.mts.teta.domain.User;
import ru.mts.teta.dto.UserDto;
import ru.mts.teta.exception.NotFoundException;
import ru.mts.teta.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CourseRepository courseRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> findUsersNotAssignedToCourse(Long courseId) {
        return userRepository.findUsersNotAssignedToCourse(courseId).stream()
                .map(userMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findUsersAssignedToCourse(Long courseId) {
        return userRepository.findUsersAssignedToCourse(courseId).stream()
                .map(userMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id).map(userMapper::mapToDto).orElseThrow(NotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.getById(id);
        for (var course : user.getCourses()) {
            course.getUsers().remove(user);
            user.getCourses().remove(course);
            courseRepository.save(course);
        }
        userRepository.delete(user);
    }

    @Override
    public User save(UserDto dto) {
        return userRepository.save(userMapper.mapToUser(dto));
    }

    @Override
    public List<UserDto> findByUsernameLike(String username) {
        return userRepository.findByUsernameLike(username + "%").stream()
                .map(userMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto createNewUser() {
        return new UserDto();
    }

    @Override
    public UserDto findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).map(userMapper::mapToDto)
                .orElseThrow(NotFoundException::new);
    }
}
