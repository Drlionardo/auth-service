package com.example.authserver.auth.provider;

import com.example.authserver.auth.authentication.EmailAuthentication;
import com.example.authserver.data.user.SecurityUser;
import com.example.authserver.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var user = (SecurityUser) authentication.getPrincipal();
        String email = user.getEmail();
        String password = String.valueOf(authentication.getCredentials());

        return new EmailAuthentication(email, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthentication.class.isAssignableFrom(authentication);
    }
}
