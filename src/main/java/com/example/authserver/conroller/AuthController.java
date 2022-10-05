package com.example.authserver.conroller;

import com.example.authserver.data.user.SecurityUser;
import com.example.authserver.data.user.dto.request.CreateUserDto;
import com.example.authserver.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public SecurityUser registerUser(@RequestBody CreateUserDto createUserDto) {
        return authenticationService.registerUser(createUserDto);
    }
}
