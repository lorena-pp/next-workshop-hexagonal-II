package com.music.streaming.catalog.infrastructure.repository;

import com.music.streaming.catalog.infrastructure.repository.entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongJpaRepository extends JpaRepository<SongEntity, String> {
    List<SongEntity> findByTitle(String title);
}
