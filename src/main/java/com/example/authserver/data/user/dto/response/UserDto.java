package com.example.authserver.data.user.dto.response;

import lombok.Builder;

@Builder
public record UserDto(Long id, String login, String email, boolean emailConfirmed) {
}
