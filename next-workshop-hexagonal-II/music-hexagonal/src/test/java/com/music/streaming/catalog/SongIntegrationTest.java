package com.music.streaming.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.streaming.catalog.infrastructure.repository.SongJpaRepository;
import com.music.streaming.catalog.infrastructure.repository.entity.SongEntity;
import com.music.streaming.catalog.infrastructure.rest.dto.request.PatchSongRequestDTO;
import com.music.streaming.catalog.infrastructure.rest.dto.request.PostSongRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeAll;
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

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class SongIntegrationTest {

    static EasyRandom EASY_RANDOM;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SongJpaRepository songJpaRepository;

    @BeforeAll
    public static void beforeAll() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(10, 24);
        parameters.collectionSizeRange(5, 10);
        EASY_RANDOM = new EasyRandom(parameters);
    }

    @BeforeEach
    public void beforeEach() {
        log.info("Resetting songs in database");
        songJpaRepository.deleteAll();
        List<SongEntity> data = EASY_RANDOM.objects(SongEntity.class, 20).toList();
        songJpaRepository.saveAll(data.stream().peek(e -> {
            e.setId(UUID.randomUUID().toString());
            e.setDurationSeconds(Math.abs(e.getDurationSeconds() == null ? 1 : e.getDurationSeconds()) + 1);
        }).toList());
    }

    @Test
    void getAllSongs_shouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/songs"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAllSongs_shouldReturn204NoContent() throws Exception {
        songJpaRepository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/songs"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void getSongById_shouldReturn200WithBody() throws Exception {
        SongEntity song = songJpaRepository.findAll().getFirst();
        mockMvc.perform(MockMvcRequestBuilders.get("/songs/" + song.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(song.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(song.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.durationSeconds").value(song.getDurationSeconds()));
    }

    @Test
    void getSongById_shouldReturn404IfNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/songs/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createSong_shouldReturn201AndLocationHeader() throws Exception {
        PostSongRequestDTO dto = PostSongRequestDTO.builder()
                .title("Never Gonna Give You Up")
                .durationSeconds(213)
                .artistId("artist-1")
                .albumId("album-1")
                .genreId("genre-1")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.matchesRegex(".*/songs/.*")));
    }

    @Test
    void createSong_shouldReturn422IfTitleIsMissing() throws Exception {
        PostSongRequestDTO dto = PostSongRequestDTO.builder()
                .durationSeconds(213)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void createSong_shouldReturn422IfDurationIsZero() throws Exception {
        PostSongRequestDTO dto = PostSongRequestDTO.builder()
                .title("Test Song")
                .durationSeconds(0)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void createSong_shouldReturn422IfDurationIsNegative() throws Exception {
        PostSongRequestDTO dto = PostSongRequestDTO.builder()
                .title("Test Song")
                .durationSeconds(-5)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void createSong_shouldReturn409IfTitleAlreadyExists() throws Exception {
        String existingTitle = songJpaRepository.findAll().getFirst().getTitle();
        PostSongRequestDTO dto = PostSongRequestDTO.builder()
                .title(existingTitle)
                .durationSeconds(200)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void deleteSong_shouldReturn204IfSuccessful() throws Exception {
        String id = songJpaRepository.findAll().getFirst().getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/songs/" + id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteSong_shouldReturn404IfNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/songs/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteSong_shouldReturn422IfInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/songs/invalid-id"))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void patchSong_shouldReturn204IfSuccessful() throws Exception {
        String id = songJpaRepository.findAll().getFirst().getId();
        PatchSongRequestDTO dto = PatchSongRequestDTO.builder()
                .title("Updated Title")
                .durationSeconds(300)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/songs/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void patchSong_shouldReturn404IfNotFound() throws Exception {
        PatchSongRequestDTO dto = PatchSongRequestDTO.builder()
                .title("Updated Title")
                .durationSeconds(300)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/songs/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void patchSong_shouldReturn422IfTitleIsEmpty() throws Exception {
        String id = songJpaRepository.findAll().getFirst().getId();
        PatchSongRequestDTO dto = PatchSongRequestDTO.builder()
                .title("")
                .durationSeconds(300)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/songs/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void patchSong_shouldReturn422IfIdIsInvalid() throws Exception {
        PatchSongRequestDTO dto = PatchSongRequestDTO.builder()
                .title("Updated Title")
                .durationSeconds(300)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/songs/not-a-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }
}
