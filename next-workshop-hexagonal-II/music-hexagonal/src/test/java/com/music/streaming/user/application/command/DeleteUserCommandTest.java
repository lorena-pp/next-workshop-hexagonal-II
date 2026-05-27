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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserCommandTest {

    @Mock
    UserRepositoryPort userRepository;

    @Test
    void handle_shouldSucceedWhenValid() {
        String id = UUID.randomUUID().toString();
        when(userRepository.getUserById(id)).thenReturn(Optional.of(User.builder().id(id).build()));

        assertDoesNotThrow(() ->
                DeleteUserCommand.builder()
                        .userRepository(userRepository)
                        .id(id)
                        .build().handle());
        verify(userRepository).deleteUser(id);
    }

    @Test
    void handle_shouldThrowInvalidUserExceptionWhenIdIsNotValidUUID() {
        assertThrows(InvalidUserException.class, () ->
                DeleteUserCommand.builder()
                        .userRepository(userRepository)
                        .id("invalid-id")
                        .build().handle());
        verifyNoInteractions(userRepository);
    }

    @Test
    void handle_shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        String id = UUID.randomUUID().toString();
        when(userRepository.getUserById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                DeleteUserCommand.builder()
                        .userRepository(userRepository)
                        .id(id)
                        .build().handle());
        verify(userRepository, never()).deleteUser(any());
    }
}
