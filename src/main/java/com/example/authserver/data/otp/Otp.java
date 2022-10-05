package com.example.authserver.data.otp;

import com.example.authserver.data.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    private Long id;

    @ManyToOne(cascade = javax.persistence.CascadeType.REMOVE, targetEntity = User.class)
    private Long userId;

    private String code;

    private Instant creationTime;

}
