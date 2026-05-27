package com.music.streaming.user.infrastructure.rest;

import com.music.streaming.user.application.command.AddFavouriteSongCommand;
import com.music.streaming.user.application.command.CreateUserCommand;
import com.music.streaming.user.application.command.DeleteUserCommand;
import com.music.streaming.user.application.command.UpdateUserCommand;
import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.application.query.GetAllUsersQuery;
import com.music.streaming.user.application.query.GetUserByIdQuery;
import com.music.streaming.user.domain.DuplicatedUserException;
import com.music.streaming.user.domain.InvalidUserException;
import com.music.streaming.user.domain.User;
import com.music.streaming.user.domain.UserNotFoundException;
import com.music.streaming.user.infrastructure.rest.dto.request.PatchUserRequestDTO;
import com.music.streaming.user.infrastructure.rest.dto.request.PostUserRequestDTO;
import com.music.streaming.user.infrastructure.rest.dto.response.GetUserResponseDTO;
import com.music.streaming.user.infrastructure.rest.mapper.UserFacadeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    final UserFacadeMapper userFacadeMapper;
    final UserRepositoryPort userRepositoryPort;
    final AddFavouriteSongCommand addFavouriteSongCommand;

    @GetMapping
    public ResponseEntity getAllUsers() {
        List<User> users = GetAllUsersQuery.builder().userRepository(userRepositoryPort).build().execute();
        if (users.isEmpty()) return ResponseEntity.noContent().build();
        List<GetUserResponseDTO> usersResponse = users.stream().map(userFacadeMapper::fromDomain).toList();
        return ResponseEntity.ok(usersResponse);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody PostUserRequestDTO userDto) {
        try {
            String id = CreateUserCommand.builder()
                    .userRepository(userRepositoryPort)
                    .username(userDto.getUsername())
                    .email(userDto.getEmail())
                    .build().handle();
            return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()).build();
        } catch (DuplicatedUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (InvalidUserException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable String id) {
        Optional<User> user = GetUserByIdQuery.builder().userRepository(userRepositoryPort).id(id).build().execute();
        return user.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(userFacadeMapper.fromDomain(user.get()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable String id, @RequestBody PatchUserRequestDTO userDto) {
        try {
            UpdateUserCommand.builder()
                    .userRepository(userRepositoryPort)
                    .id(id)
                    .username(userDto.getUsername())
                    .email(userDto.getEmail())
                    .build().handle();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidUserException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        try {
            DeleteUserCommand.builder().userRepository(userRepositoryPort).id(id).build().handle();
        } catch (InvalidUserException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/favorites/{songId}")
    public ResponseEntity<Void> addFavouriteSong(@PathVariable String id, @PathVariable String songId) throws UserNotFoundException {
        addFavouriteSongCommand.handle(id, songId);
        return ResponseEntity.noContent().build();
    }
}
