package com.music.streaming.user.infrastructure.rest.mapper;

import com.music.streaming.user.domain.User;
import com.music.streaming.user.infrastructure.rest.dto.request.PostUserRequestDTO;
import com.music.streaming.user.infrastructure.rest.dto.response.GetUserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserFacadeMapper {
    GetUserResponseDTO fromDomain(User in);
    User fromPostUserRequestDTO(PostUserRequestDTO userDto);
}
