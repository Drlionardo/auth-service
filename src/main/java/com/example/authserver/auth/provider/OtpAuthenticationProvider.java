package com.example.authserver.auth.provider;

import com.example.authserver.auth.authentication.OtpAuthentication;
import com.example.authserver.data.user.UserService;
import com.example.authserver.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {
    private AuthenticationService authenticationService;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String otp = (String) authentication.getCredentials();

        if (authenticationService.checkOtp(email, otp)) {
            var user = userService.getUserByEmail(email);
            return new OtpAuthentication(user, null, List.of());
        } else {
            throw new BadCredentialsException("Invalid otp or email");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
