package com.example.authserver.auth.provider;

import com.example.authserver.auth.authentication.EmailAuthentication;
import com.example.authserver.data.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class EmailAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        var user = userService.getUserByEmail(email);

        if (passwordEncoder.matches(password, user.getPassword())) {
            return new EmailAuthentication(email, password, List.of());
        } else {
            throw new BadCredentialsException("Bad email or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthentication.class.isAssignableFrom(authentication);
    }
}
