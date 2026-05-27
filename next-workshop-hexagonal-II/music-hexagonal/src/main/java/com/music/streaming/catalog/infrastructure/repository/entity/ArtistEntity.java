package com.music.streaming.catalog.infrastructure.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ArtistEntity {
    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String bio;
}
