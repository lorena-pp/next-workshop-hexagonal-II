package com.music.streaming.user.infrastructure.repository.mapper;

import com.music.streaming.user.domain.User;
import com.music.streaming.user.infrastructure.repository.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    UserEntity fromDomain(User in);

    User toDomain(UserEntity in);
}
