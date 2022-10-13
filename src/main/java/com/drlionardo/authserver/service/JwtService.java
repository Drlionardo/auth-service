package com.drlionardo.authserver.service;

import com.drlionardo.authserver.data.jwt.RevokeRefreshTokenRepository;
import com.drlionardo.authserver.data.jwt.RevokedRefreshToken;
import com.drlionardo.authserver.data.user.SecurityUser;
import com.drlionardo.authserver.exception.TokenRevokedException;
import com.drlionardo.authserver.property.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
@AllArgsConstructor
public class JwtService {
    private final RevokeRefreshTokenRepository revokeRefreshTokenRepository;
    private final JwtProperties jwtProperties;

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
                .setIssuer(jwtProperties.getIssuer())
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
                .expiresAt(claims.getExpiration().toInstant())
                .build();

        revokeRefreshTokenRepository.save(revokedToken);
    }

    private Claims getClaims(String refreshToken) {
        SecretKey key = getSigningKey();
        var jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(jwtProperties.getIssuer())
                .build();

        return jwtParser.parseClaimsJws(refreshToken).getBody();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSigningKey().getBytes(StandardCharsets.UTF_8));
    }
}
