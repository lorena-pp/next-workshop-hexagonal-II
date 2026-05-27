package com.music.streaming.user.application.command;

import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.domain.InvalidUserException;
import com.music.streaming.user.domain.User;
import com.music.streaming.user.domain.UserNotFoundException;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import java.util.UUID;

@SuperBuilder
public class UpdateUserCommand {
    @NonNull
    final UserRepositoryPort userRepository;
    @NonNull
    final String id;
    final String username;
    final String email;

    public void handle() throws UserNotFoundException, InvalidUserException {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(email)) throw new InvalidUserException();
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUserException();
        }
        if (userRepository.getUserById(id).isEmpty()) throw new UserNotFoundException();
        userRepository.updateUser(User.builder().id(id).username(username).email(email).build());
    }
}
