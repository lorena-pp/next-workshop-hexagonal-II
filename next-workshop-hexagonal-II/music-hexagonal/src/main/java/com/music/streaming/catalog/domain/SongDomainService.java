package com.music.streaming.catalog.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SongDomainService {

    public void validate(String title, Integer durationSeconds) throws InvalidSongException {
        if (!StringUtils.hasText(title)) throw new InvalidSongException();
        if (durationSeconds == null || durationSeconds <= 0) throw new InvalidSongException();
    }
}
