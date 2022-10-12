package com.example.authserver.data.otp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    private Long userId;

    private String code;

    private Instant expiresAt;

}
