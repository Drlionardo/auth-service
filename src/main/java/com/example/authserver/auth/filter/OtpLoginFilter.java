package com.example.authserver.auth.filter;

import com.example.authserver.auth.authentication.EmailAuthentication;
import com.example.authserver.auth.authentication.OtpAuthentication;
import com.example.authserver.auth.provider.EmailAuthenticationProvider;
import com.example.authserver.auth.provider.OtpAuthenticationProvider;
import com.example.authserver.data.login.LoginStage;
import com.example.authserver.data.login.LoginWithEmailDto;
import com.example.authserver.data.login.LoginWithOtpDto;
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
public class OtpLoginFilter extends OncePerRequestFilter {
    private final OtpAuthenticationProvider otpAuthenticationProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        request = new CachedBodyHttpServletRequest(request);

        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        var dto = new ObjectMapper().readValue(body, LoginWithOtpDto.class);
        var securityUser = userService.getUserByEmail(dto.email());

        processOtpStage(dto, securityUser);

        filterChain.doFilter(request, response);
    }

    private void processOtpStage(LoginWithOtpDto dto, SecurityUser securityUser) {
        var authentication = new OtpAuthentication(securityUser, dto.otp(), List.of());
        otpAuthenticationProvider.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login/otp");
    }
}
