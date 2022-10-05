package com.example.authserver.data.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    @Query(value = """
            SELECT FIRST o.* 
            FROM otp o
             WHERE o.user_id = :userId
             AND o.code = :code
             AND o.creationDate >= timestamp (:creationTime)
            """, nativeQuery = true)
    Optional<Otp> findByUserIdAndCodeAndCreationDate(Long userId, String code, Instant creationTime);
}
