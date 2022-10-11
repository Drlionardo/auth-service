package com.example.authserver.data.jwt;

public record JwtPair(
        String token,
        String refreshToken) {
}
