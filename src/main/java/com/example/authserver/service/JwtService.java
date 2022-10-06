package com.example.authserver.service;

import com.example.authserver.data.jwt.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.example.authserver.data.user.SecurityUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.signing.key}")
    private String signingKey;

    private Jwt buildJwt(SecurityUser user, Duration tokenTtl) {
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        return new Jwt(Jwts.builder()
                .setExpiration(Date.from(Instant.now().plus(tokenTtl)))
                .setClaims(Map.of(
                        "username", user.getUsername(),
                        "client_id", user.getId()
                ))
                .signWith(key)
                .compact());
    }
}
