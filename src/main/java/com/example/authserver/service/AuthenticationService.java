package com.example.authserver.service;

import com.example.authserver.data.jwt.JwtPair;
import com.example.authserver.data.otp.OtpService;
import com.example.authserver.data.user.SecurityUser;
import com.example.authserver.data.user.UserMapper;
import com.example.authserver.data.user.UserService;
import com.example.authserver.data.user.dto.request.ConfirmUserEmailDto;
import com.example.authserver.data.user.dto.request.CreateUserDto;
import com.example.authserver.exception.BadTokenFormatException;
import com.example.authserver.exception.UserAlreadyExistsException;
import com.example.authserver.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final OtpService otpService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public void sendOtpToEmail(SecurityUser user) {
        otpService.sendOtpToEmail(user.getId(), user.getEmail());
    }

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
            throw new UserAlreadyExistsException(createUserDto.email());
        }
    }

    public void confirmEmail(ConfirmUserEmailDto dto) {
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

    public boolean checkOtp(String email, String otp) {
        return otpService.checkOtp(email, otp);
    }

    public JwtPair generateJwtPair(SecurityUser user) {
        var accessToken = jwtService.buildJwt(user);
        var refreshToken = jwtService.buildRefreshJwt(user);
        return new JwtPair(accessToken, refreshToken);
    }

    @Transactional
    public JwtPair refreshToken(String header) {
        String refreshToken = getJwtFromHeader(header);

        Long userId = jwtService.getUserIdByRefreshToken(refreshToken);
        var user = userService.getUserById(userId);

        return generateJwtPair(user);
    }

    private String getJwtFromHeader(String header) {
        if (header.startsWith("Bearer ")) {
            return header.substring(7);
        } else {
            throw new BadTokenFormatException(header);
        }
    }
}
