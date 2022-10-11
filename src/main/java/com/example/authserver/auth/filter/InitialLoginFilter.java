package com.example.authserver.auth.filter;

import com.example.authserver.auth.authentication.EmailAuthentication;
import com.example.authserver.auth.authentication.OtpAuthentication;
import com.example.authserver.auth.provider.EmailAuthenticationProvider;
import com.example.authserver.auth.provider.OtpAuthenticationProvider;
import com.example.authserver.data.login.LoginStage;
import com.example.authserver.data.login.LoginWithEmailDto;
import com.example.authserver.data.user.SecurityUser;
import com.example.authserver.data.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class InitialLoginFilter extends OncePerRequestFilter {
    private final EmailAuthenticationProvider emailAndPasswordAuthenticationProvider;
    private final OtpAuthenticationProvider otpAuthenticationProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        request = new CachedBodyHttpServletRequest(request);
        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        var dto = new ObjectMapper().readValue(body, LoginWithEmailDto.class);
        var securityUser = userService.getUserByEmail(dto.email());

        processInitialStage(dto, securityUser);

        filterChain.doFilter(request, response);
    }


    private void processInitialStage(LoginWithEmailDto dto, SecurityUser securityUser) {
        var authentication = new EmailAuthentication(securityUser, dto.password());
        emailAndPasswordAuthenticationProvider.authenticate(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}
