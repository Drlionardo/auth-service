package com.example.authserver.data.login;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record LoginWithEmailDto(
        @Email String email,
        String password,
        String otp,
        @NotNull LoginStage loginStage) {
}

