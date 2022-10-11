package com.example.authserver.conroller;

import com.example.authserver.data.jwt.JwtPair;
import com.example.authserver.data.login.LoginWithEmailDto;
import com.example.authserver.data.login.LoginWithOtpDto;
import com.example.authserver.data.user.SecurityUser;
import com.example.authserver.data.user.dto.request.ConfirmUserEmailDto;
import com.example.authserver.data.user.dto.request.CreateUserDto;
import com.example.authserver.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public SecurityUser registerUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return authenticationService.registerUser(createUserDto);
    }

    @PostMapping("/register/confirm")
    public ResponseEntity<String> confirmEmail(@Valid @RequestBody ConfirmUserEmailDto dto) {
        authenticationService.confirmEmail(dto);
        return ResponseEntity.ok()
                .header("Authorization", "todo")
                .body(String.format("Successfully confirmed email %s", dto.email()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> initialLogin(@Valid @RequestBody LoginWithEmailDto dto,
                                          @AuthenticationPrincipal SecurityUser user) {
        authenticationService.sendOtpToEmail(user);
        return new ResponseEntity<>("Otp code was send to email", HttpStatus.OK);
    }

    @PostMapping("/login/otp")
    public JwtPair initialLogin(@Valid @RequestBody LoginWithOtpDto dto, @AuthenticationPrincipal SecurityUser user) {
        return authenticationService.generateJwtPair(user);
    }

    @PostMapping("/refresh")
    public JwtPair refreshToken(@RequestHeader("Authorization") String refreshToken) {
        return authenticationService.refreshToken(refreshToken);

    }
}
