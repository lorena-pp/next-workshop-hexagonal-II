package com.music.streaming.catalog.infrastructure.rest.dto.response;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@Getter
@ToString
public class GetSongResponseDTO {
    final String id;
    final String title;
    final Integer durationSeconds;
    final String artistId;
    final String albumId;
    final String genreId;
}
