package com.music.streaming.catalog.infrastructure.repository.mapper;

import com.music.streaming.catalog.domain.Song;
import com.music.streaming.catalog.infrastructure.repository.entity.SongEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongEntityMapper {
    SongEntity fromDomain(Song in);

    Song toDomain(SongEntity in);
}
