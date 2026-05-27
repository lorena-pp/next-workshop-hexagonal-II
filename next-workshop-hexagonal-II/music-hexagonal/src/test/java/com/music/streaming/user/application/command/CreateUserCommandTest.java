package com.music.streaming.user.application.command;

import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.domain.DuplicatedUserException;
import com.music.streaming.user.domain.InvalidUserException;
import com.music.streaming.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserCommandTest {

    @Mock
    UserRepositoryPort userRepository;

    @Test
    void handle_shouldReturnIdWhenValid() throws InvalidUserException, DuplicatedUserException {
        when(userRepository.getUserByEmail("john@music.com")).thenReturn(Optional.empty());

        String id = CreateUserCommand.builder()
                .userRepository(userRepository)
                .username("john")
                .email("john@music.com")
                .build().handle();

        assertNotNull(id);
        assertDoesNotThrow(() -> UUID.fromString(id));
        verify(userRepository).createUser(any(User.class));
    }

    @Test
    void handle_shouldThrowInvalidUserExceptionWhenUsernameIsEmpty() {
        assertThrows(InvalidUserException.class, () ->
                CreateUserCommand.builder()
                        .userRepository(userRepository)
                        .username("")
                        .email("john@music.com")
                        .build().handle());
        verifyNoInteractions(userRepository);
    }

    @Test
    void handle_shouldThrowInvalidUserExceptionWhenUsernameIsNull() {
        assertThrows(InvalidUserException.class, () ->
                CreateUserCommand.builder()
                        .userRepository(userRepository)
                        .username(null)
                        .email("john@music.com")
                        .build().handle());
        verifyNoInteractions(userRepository);
    }

    @Test
    void handle_shouldThrowInvalidUserExceptionWhenEmailIsEmpty() {
        assertThrows(InvalidUserException.class, () ->
                CreateUserCommand.builder()
                        .userRepository(userRepository)
                        .username("john")
                        .email("")
                        .build().handle());
        verifyNoInteractions(userRepository);
    }

    @Test
    void handle_shouldThrowInvalidUserExceptionWhenEmailIsNull() {
        assertThrows(InvalidUserException.class, () ->
                CreateUserCommand.builder()
                        .userRepository(userRepository)
                        .username("john")
                        .email(null)
                        .build().handle());
        verifyNoInteractions(userRepository);
    }

    @Test
    void handle_shouldThrowDuplicatedUserExceptionWhenEmailAlreadyExists() {
        when(userRepository.getUserByEmail("john@music.com"))
                .thenReturn(Optional.of(User.builder().username("john").email("john@music.com").build()));

        assertThrows(DuplicatedUserException.class, () ->
                CreateUserCommand.builder()
                        .userRepository(userRepository)
                        .username("john")
                        .email("john@music.com")
                        .build().handle());
        verify(userRepository, never()).createUser(any());
    }
}
