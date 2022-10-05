package com.example.authserver.data.user.dto.request;

public record ValidateUserEmailDto(
        String email,
        String code) {
}
