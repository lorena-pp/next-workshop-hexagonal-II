package com.music.streaming.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.streaming.user.infrastructure.repository.UserJpaRepository;
import com.music.streaming.user.infrastructure.repository.entity.UserEntity;
import com.music.streaming.user.infrastructure.rest.dto.request.PatchUserRequestDTO;
import com.music.streaming.user.infrastructure.rest.dto.request.PostUserRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    public void beforeEach() {
        log.info("Resetting users in database");
        userJpaRepository.deleteAll();
        List<UserEntity> users = IntStream.range(0, 10)
                .<UserEntity>mapToObj(i -> UserEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .username("username_" + i)
                        .email("user" + i + "@music.com")
                        .build())
                .toList();
        userJpaRepository.saveAll(users);
    }

    @Test
    void getAllUsers_shouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAllUsers_shouldReturn204NoContent() throws Exception {
        userJpaRepository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void getUserById_shouldReturn200WithBody() throws Exception {
        UserEntity user = userJpaRepository.findAll().getFirst();
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(user.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void getUserById_shouldReturn404IfNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createUser_shouldReturn201AndLocationHeader() throws Exception {
        PostUserRequestDTO dto = PostUserRequestDTO.builder()
                .username("newuser")
                .email("newuser@music.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.matchesRegex(".*/users/.*")));
    }

    @Test
    void createUser_shouldReturn422IfUsernameIsMissing() throws Exception {
        PostUserRequestDTO dto = PostUserRequestDTO.builder()
                .email("newuser@music.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void createUser_shouldReturn422IfEmailIsMissing() throws Exception {
        PostUserRequestDTO dto = PostUserRequestDTO.builder()
                .username("newuser")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void createUser_shouldReturn409IfEmailAlreadyExists() throws Exception {
        String existingEmail = userJpaRepository.findAll().getFirst().getEmail();
        PostUserRequestDTO dto = PostUserRequestDTO.builder()
                .username("anotheruser")
                .email(existingEmail)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void deleteUser_shouldReturn204IfSuccessful() throws Exception {
        String id = userJpaRepository.findAll().getFirst().getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteUser_shouldReturn404IfNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteUser_shouldReturn422IfInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/invalid-id"))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void patchUser_shouldReturn204IfSuccessful() throws Exception {
        String id = userJpaRepository.findAll().getFirst().getId();
        PatchUserRequestDTO dto = PatchUserRequestDTO.builder()
                .username("updateduser")
                .email("updated@music.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void patchUser_shouldReturn404IfNotFound() throws Exception {
        PatchUserRequestDTO dto = PatchUserRequestDTO.builder()
                .username("updateduser")
                .email("updated@music.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void patchUser_shouldReturn422IfUsernameIsEmpty() throws Exception {
        String id = userJpaRepository.findAll().getFirst().getId();
        PatchUserRequestDTO dto = PatchUserRequestDTO.builder()
                .username("")
                .email("updated@music.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void patchUser_shouldReturn422IfIdIsInvalid() throws Exception {
        PatchUserRequestDTO dto = PatchUserRequestDTO.builder()
                .username("updateduser")
                .email("updated@music.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/not-a-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }
}
