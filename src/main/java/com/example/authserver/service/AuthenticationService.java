package com.example.authserver.service;

import com.example.authserver.data.user.UserMapper;
import com.example.authserver.data.user.SecurityUser;
import com.example.authserver.data.user.UserService;
import com.example.authserver.data.user.dto.request.CreateUserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    public SecurityUser registerUser(CreateUserDto createUserDto) {
        validateNewUser(createUserDto);
        return userMapper.map(userService.registerUser(createUserDto));
    }

    private void validateNewUser(CreateUserDto createUserDto) {
        var userOptional = userService.findUserByEmail(createUserDto.email());
        if(userOptional.isPresent()) {
            throw new RuntimeException();
        }
    }
}
