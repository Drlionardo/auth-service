package com.example.authserver.data.login;

public record LoginWithLoginDto(
        String login,
        String email,
        String password,
        String otp,
        LoginStage loginStage) {
}

