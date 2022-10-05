package com.example.authserver.data.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Long id;

    private String login;

    private String email;

    private String password;

    private boolean isEmailConfirmed;
}
