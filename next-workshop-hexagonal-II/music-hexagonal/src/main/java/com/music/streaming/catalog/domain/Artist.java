package com.music.streaming.catalog.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Artist {
    @Builder.Default
    final String id = UUID.randomUUID().toString();
    @With
    String name;
    @With
    String bio;
}
