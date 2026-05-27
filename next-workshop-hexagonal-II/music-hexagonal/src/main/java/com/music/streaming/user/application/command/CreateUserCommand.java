package com.music.streaming.user.application.command;

import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.domain.DuplicatedUserException;
import com.music.streaming.user.domain.InvalidUserException;
import com.music.streaming.user.domain.User;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import java.util.UUID;

@SuperBuilder
public class CreateUserCommand {
    @NonNull
    final UserRepositoryPort userRepository;
    final String username;
    final String email;

    public String handle() throws InvalidUserException, DuplicatedUserException {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(email)) throw new InvalidUserException();
        if (userRepository.getUserByEmail(email).isPresent()) throw new DuplicatedUserException();
        User user = User.builder().id(UUID.randomUUID().toString()).username(username).email(email).build();
        userRepository.createUser(user);
        return user.getId();
    }
}
