package com.example.authserver.auth.filter;

        import com.example.authserver.auth.authentication.EmailAuthentication;
        import com.example.authserver.auth.authentication.OtpAuthentication;
        import com.example.authserver.data.login.LoginStage;
        import com.example.authserver.data.login.LoginWithEmailDto;
        import com.example.authserver.data.user.UserService;
        import com.example.authserver.service.JwtService;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import lombok.AllArgsConstructor;
        import org.springframework.security.authentication.AuthenticationManager;
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
public class SecondStageLoginFilter extends OncePerRequestFilter {
    private final AuthenticationManager manager;
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        var dto = new ObjectMapper().readValue(body, LoginWithEmailDto.class);

        if(dto.loginStage() == LoginStage.SECOND) {
            processSecondStage(response, dto);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void processSecondStage(HttpServletResponse response, LoginWithEmailDto dto) {
        var authentication = new OtpAuthentication(dto.email(), dto.otp());
        manager.authenticate(authentication);
        setAuthorizationHeader(response, dto);
    }

    private void setAuthorizationHeader(HttpServletResponse response, LoginWithEmailDto dto) {
        var user = userService.getUserByEmail(dto.email());
        String jwt = jwtService.buildJwt(user);
        response.setHeader("Authorization", jwt);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}
