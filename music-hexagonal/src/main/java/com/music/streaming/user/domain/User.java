package com.music.streaming.user.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User {
    @Builder.Default
    final String id = UUID.randomUUID().toString();
    @With
    String username;
    @With
    String email;
    @With
    private List<String> favouriteSongIds = new ArrayList<>();

    public void addFavouriteSong(String songId) {
        if (!this.favouriteSongIds.contains(songId)) {
            this.favouriteSongIds.add(songId);
        }
    }

}
