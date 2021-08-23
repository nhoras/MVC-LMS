package ru.mts.teta.service;

import ru.mts.teta.domain.User;
import ru.mts.teta.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findUsersNotAssignedToCourse(Long courseId);

    List<UserDto> findUsersAssignedToCourse(Long courseId);

    UserDto findById(Long id);

    void deleteById(Long id);

    User save(UserDto userDto);

    List<UserDto> findByUsernameLike(String username);

    UserDto createNewUser();

    UserDto findUserByUsername(String username);
}
