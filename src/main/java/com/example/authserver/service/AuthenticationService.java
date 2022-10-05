package com.example.authserver.service;

import com.example.authserver.data.otp.OtpService;
import com.example.authserver.data.user.UserMapper;
import com.example.authserver.data.user.SecurityUser;
import com.example.authserver.data.user.UserService;
import com.example.authserver.data.user.dto.request.CreateUserDto;
import com.example.authserver.data.user.dto.request.ValidateUserEmailDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final OtpService otpService;
    private final UserMapper userMapper;

    @Transactional
    public SecurityUser registerUser(CreateUserDto createUserDto) {
        validateNewUser(createUserDto);

        var user = userService.registerUser(createUserDto);
        otpService.sendOtpToEmail(user.getId(), user.getEmail());

        return userMapper.map(user);
    }

    private void validateNewUser(CreateUserDto createUserDto) {
        var userOptional = userService.findUserByEmail(createUserDto.email());
        if (userOptional.isPresent()) {
            throw new RuntimeException();
        }
    }

    public void confirmEmail(ValidateUserEmailDto dto) {
        var user = userService.getUserByEmail(dto.email());
        if (user.isEmailConfirmed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User email already validated");
        }

        if (otpService.checkOtp(dto.email(), dto.code())) {
            userService.setEmailConfirmed(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad code from email");
        }
    }
}
