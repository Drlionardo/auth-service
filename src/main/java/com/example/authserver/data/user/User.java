package com.example.authserver.data.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usr")
public class User {
    @Id
    private Long id;

    private String login;

    private String email;

    private String password;

    private boolean emailConfirmed;
}
