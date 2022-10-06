package com.example.authserver.data.jwt;

public record JwtPair(
        Jwt token,
        Jwt refreshToken) {
}
