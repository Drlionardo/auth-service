package com.example.authserver.data.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "revoked_token")
public class RevokedRefreshToken {
    @Id
    private Long userId;

    private String token;

    private Instant expiresAt;

}
