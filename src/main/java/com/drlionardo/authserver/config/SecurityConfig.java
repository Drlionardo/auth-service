package com.drlionardo.authserver.config;

import com.drlionardo.authserver.auth.filter.InitialLoginFilter;
import com.drlionardo.authserver.auth.filter.OtpLoginFilter;
import com.drlionardo.authserver.auth.provider.EmailAuthenticationProvider;
import com.drlionardo.authserver.auth.provider.OtpAuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final EmailAuthenticationProvider emailAuthenticationProvider;
    private final OtpAuthenticationProvider otpAuthenticationProvider;
    private final InitialLoginFilter initialLoginFilter;
    private final OtpLoginFilter otpLoginFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .mvcMatchers("/register**", "/refresh").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                .authenticationProvider(emailAuthenticationProvider)
                .authenticationProvider(otpAuthenticationProvider)
                .addFilterBefore(initialLoginFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(otpLoginFilter, BasicAuthenticationFilter.class);
        return http.build();
    }
}
