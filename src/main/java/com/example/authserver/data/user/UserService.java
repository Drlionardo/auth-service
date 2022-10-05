package com.example.authserver.data.user;

import com.example.authserver.data.user.dto.request.CreateUserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return findUserByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(CreateUserDto createUserDto) {
        var newUser = User.builder()
                .email(createUserDto.email())
                .login(createUserDto.login())
                .password(passwordEncoder.encode(createUserDto.password()))
                .build();

        return userRepository.save(newUser);
    }

    public void setEmailConfirmed(User user) {
        user.setEmailConfirmed(true);
        userRepository.save(user);
    }
}
