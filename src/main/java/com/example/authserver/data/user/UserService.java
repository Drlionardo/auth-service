package com.example.authserver.data.user;

import com.example.authserver.data.user.dto.request.CreateUserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User loadUserByEmail(String email) {
        return findUserByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(CreateUserDto createUserDto) {
        var newUser = User.builder()
                .email(createUserDto.email())
                .login(createUserDto.login())
                .password(createUserDto.password())
                .build();

        return userRepository.save(newUser);
    }
}
