package com.music.streaming.catalog.infrastructure.rest.mapper;

import com.music.streaming.catalog.domain.Song;
import com.music.streaming.catalog.infrastructure.rest.dto.request.PostSongRequestDTO;
import com.music.streaming.catalog.infrastructure.rest.dto.response.GetSongResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongFacadeMapper {
    GetSongResponseDTO fromDomain(Song in);
    Song fromPostSongRequestDTO(PostSongRequestDTO songDto);
}
