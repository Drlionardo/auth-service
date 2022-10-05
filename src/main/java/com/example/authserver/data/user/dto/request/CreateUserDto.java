package com.example.authserver.data.user.dto.request;

public record CreateUserDto(
        String login,
        String email,
        String password) {
}
