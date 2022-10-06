package com.example.authserver.auth.provider;

import com.example.authserver.auth.authentication.OtpAuthentication;
import com.example.authserver.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {
    private AuthenticationService authenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String otp = String.valueOf(authentication.getCredentials());

        if (authenticationService.checkOtp(email, otp)) {
            return new OtpAuthentication(email, otp);
        } else {
            throw new BadCredentialsException("Invalid otp or email");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
