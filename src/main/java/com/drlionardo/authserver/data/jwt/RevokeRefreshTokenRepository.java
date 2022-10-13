package com.drlionardo.authserver.data.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RevokeRefreshTokenRepository extends JpaRepository<RevokedRefreshToken, Long> {
    Optional<RevokedRefreshToken> findByToken(String token);
}
