package com.example.authserver.data.user.dto.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record CreateUserDto(
        @NotBlank
        @Length(min = 3)
        String login,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Length(min = 6)
        String password) {
}
