package com.example.authserver.service;

import com.example.authserver.data.jwt.RevokeRefreshTokenRepository;
import com.example.authserver.data.jwt.RevokedRefreshToken;
import com.example.authserver.data.user.SecurityUser;
import com.example.authserver.exception.TokenRevokedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.signing.key}")
    private String signingKey;

    @Value("${jwt.issuer}")
    private String issuer;

    private final RevokeRefreshTokenRepository revokeRefreshTokenRepository;

    public JwtService(RevokeRefreshTokenRepository revokeRefreshTokenRepository) {
        this.revokeRefreshTokenRepository = revokeRefreshTokenRepository;
    }

    public String buildJwt(SecurityUser user) {
        return buildJwt(user, Duration.of(1, ChronoUnit.HOURS));
    }

    public String buildRefreshJwt(SecurityUser user) {
        return buildJwt(user, Duration.of(7, ChronoUnit.DAYS));
    }

    private String buildJwt(SecurityUser user, Duration tokenTtl) {
        SecretKey key = getSigningKey();
        return Jwts.builder()
                .setClaims(Map.of(
                        "username", user.getUsername(),
                        "user_id", user.getId()))
                .setIssuer(issuer)
                .signWith(key)
                .setExpiration(Date.from(Instant.now().plus(tokenTtl)))
                .compact();
    }

    public Long getUserIdByRefreshToken(String refreshToken) {
        if (revokeRefreshTokenRepository.findByToken(refreshToken).isPresent()) {
            throw new TokenRevokedException(refreshToken);
        }
        Claims claims = getClaims(refreshToken);
        revokeRefreshToken(refreshToken, claims);

        return claims.get("user_id", Long.class);
    }

    private void revokeRefreshToken(String refreshToken, Claims claims) {
        var revokedToken = RevokedRefreshToken.builder()
                .userId(claims.get("user_id", Long.class))
                .token(refreshToken)
                .expirationTimestamp(claims.getExpiration().toInstant())
                .build();

        revokeRefreshTokenRepository.save(revokedToken);
    }

    private Claims getClaims(String refreshToken) {
        SecretKey key = getSigningKey();
        var jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(issuer)
                .build();

        return jwtParser.parseClaimsJws(refreshToken).getBody();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
    }
}
