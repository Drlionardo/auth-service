package com.example.authserver.data.user;

import com.example.authserver.data.user.dto.response.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public SecurityUser map(User user) {
        return new SecurityUser(user);
    }

    public UserDto map(SecurityUser user) {
        return UserDto.builder()
                .login(user.getUsername())
                .email(user.getEmail())
                .emailConfirmed(user.isEmailConfirmed())
                .build();
    }

}
