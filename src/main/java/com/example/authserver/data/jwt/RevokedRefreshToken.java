package com.example.authserver.data.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevokedRefreshToken {
    @Id
    private Long userId;

    private String token;

    private Instant expirationTimestamp;

}
