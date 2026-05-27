package com.music.streaming.catalog.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Album {
    @Builder.Default
    final String id = UUID.randomUUID().toString();
    @With
    String title;
    @With
    String artistId;
    @With
    Integer releaseYear;
}
