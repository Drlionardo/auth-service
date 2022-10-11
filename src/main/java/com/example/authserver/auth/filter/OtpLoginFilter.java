package com.example.authserver.auth.filter;

import com.example.authserver.auth.authentication.OtpAuthentication;
import com.example.authserver.data.login.LoginWithOtpDto;
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

@Component
@AllArgsConstructor
public class OtpLoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        request = new CachedBodyHttpServletRequest(request);

        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        var dto = new ObjectMapper().readValue(body, LoginWithOtpDto.class);

        processOtpStage(dto);

        filterChain.doFilter(request, response);
    }

    private void processOtpStage(LoginWithOtpDto dto) {
        var authentication = new OtpAuthentication(dto.email(), dto.otp());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login/otp");
    }
}
