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
public class SongEntity {
    @Id
    private String id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer durationSeconds;
    @Column
    private String artistId;
    @Column
    private String albumId;
    @Column
    private String genreId;
}
