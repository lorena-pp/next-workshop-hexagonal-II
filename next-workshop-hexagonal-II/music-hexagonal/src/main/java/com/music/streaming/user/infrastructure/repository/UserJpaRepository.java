package com.music.streaming.user.infrastructure.repository;

import com.music.streaming.user.infrastructure.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    List<UserEntity> findByEmail(String email);
}
