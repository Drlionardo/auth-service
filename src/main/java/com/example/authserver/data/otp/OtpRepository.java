package com.example.authserver.data.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUserIdAndCodeAndExpirationTimestampBefore(Long userId, String code, Instant expirationTimestamp);
}
