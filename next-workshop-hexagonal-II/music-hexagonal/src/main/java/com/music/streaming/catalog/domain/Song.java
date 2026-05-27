package com.music.streaming.catalog.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Song {
    @Builder.Default
    final String id = UUID.randomUUID().toString();
    @With
    String title;
    @With
    Integer durationSeconds;
    @With
    String artistId;
    @With
    String albumId;
    @With
    String genreId;
}
