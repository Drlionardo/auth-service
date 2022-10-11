package com.example.authserver.data.login;

import io.micrometer.core.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record LoginWithOtpDto(
        @Email @NotNull String email,
        @Nullable String otp) {
}

