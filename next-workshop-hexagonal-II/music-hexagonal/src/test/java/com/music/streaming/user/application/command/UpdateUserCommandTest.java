package com.music.streaming.user.application.command;

import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.domain.InvalidUserException;
import com.music.streaming.user.domain.User;
import com.music.streaming.user.domain.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserCommandTest {

    @Mock
    UserRepositoryPort userRepository;

    @Test
    void handle_shouldSucceedWhenValid() {
        String id = UUID.randomUUID().toString();
        when(userRepository.getUserById(id))
                .thenReturn(Optional.of(User.builder().id(id).username("old").email("old@music.com").build()));

        assertDoesNotThrow(() ->
                UpdateUserCommand.builder()
                        .userRepository(userRepository)
                        .id(id)
                        .username("new")
                        .email("new@music.com")
                        .build().handle());
        verify(userRepository).updateUser(any(User.class));
    }

    @Test
    void handle_shouldThrowInvalidUserExceptionWhenUsernameIsEmpty() {
        assertThrows(InvalidUserException.class, () ->
                UpdateUserCommand.builder()
                        .userRepository(userRepository)
                        .id(UUID.randomUUID().toString())
                        .username("")
                        .email("john@music.com")
                        .build().handle());
        verifyNoInteractions(userRepository);
    }

    @Test
    void handle_shouldThrowInvalidUserExceptionWhenEmailIsEmpty() {
        assertThrows(InvalidUserException.class, () ->
                UpdateUserCommand.builder()
                        .userRepository(userRepository)
                        .id(UUID.randomUUID().toString())
                        .username("john")
                        .email("")
                        .build().handle());
        verifyNoInteractions(userRepository);
    }

    @Test
    void handle_shouldThrowInvalidUserExceptionWhenIdIsNotValidUUID() {
        assertThrows(InvalidUserException.class, () ->
                UpdateUserCommand.builder()
                        .userRepository(userRepository)
                        .id("not-a-valid-uuid")
                        .username("john")
                        .email("john@music.com")
                        .build().handle());
    }

    @Test
    void handle_shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        String id = UUID.randomUUID().toString();
        when(userRepository.getUserById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                UpdateUserCommand.builder()
                        .userRepository(userRepository)
                        .id(id)
                        .username("john")
                        .email("john@music.com")
                        .build().handle());
        verify(userRepository, never()).updateUser(any());
    }
}
