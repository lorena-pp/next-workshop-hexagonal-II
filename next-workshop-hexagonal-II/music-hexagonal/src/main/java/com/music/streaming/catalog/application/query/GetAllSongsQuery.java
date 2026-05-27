package com.music.streaming.catalog.application.query;

import com.music.streaming.catalog.application.port.SongRepository;
import com.music.streaming.catalog.domain.Song;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class GetAllSongsQuery {
    @NonNull
    private SongRepository songRepository;

    public List<Song> execute() {
        return songRepository.getSongs();
    }
}
