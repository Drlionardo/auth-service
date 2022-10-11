package com.example.authserver.data.user.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record ConfirmUserEmailDto(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String code) {
}
