package com.example.authserver.data.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;

    private String login;

    private String email;

    private String password;

    private boolean isEmailValidated;
}
