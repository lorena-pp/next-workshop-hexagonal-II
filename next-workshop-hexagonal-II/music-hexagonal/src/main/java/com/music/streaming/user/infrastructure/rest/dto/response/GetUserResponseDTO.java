package com.music.streaming.user.infrastructure.rest.dto.response;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@Getter
@ToString
public class GetUserResponseDTO {
    final String id;
    final String username;
    final String email;
}
