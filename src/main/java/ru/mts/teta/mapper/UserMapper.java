package ru.mts.teta.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mts.teta.domain.User;
import ru.mts.teta.dto.UserDto;

@Service
public class UserMapper {

    private final PasswordEncoder encoder;

    @Autowired
    public UserMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public UserDto mapToDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getRoles());
    }

    public User mapToUser(UserDto dto) {
        return new User(dto.getId(), dto.getUsername(), encoder.encode(dto.getPassword()), dto.getRoles());
    }
}
