package com.drlionardo.authserver.data.jwt;

public record JwtPair(
        String token,
        String refreshToken) {
}
