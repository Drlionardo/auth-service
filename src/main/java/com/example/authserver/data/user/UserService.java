package com.example.authserver.data.user;

import com.example.authserver.data.user.dto.request.CreateUserDto;
import com.example.authserver.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public SecurityUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::map)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
    public SecurityUser getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::map)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Optional<SecurityUser> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::map);
    }

    public User registerUser(CreateUserDto createUserDto) {
        var newUser = User.builder()
                .email(createUserDto.email())
                .login(createUserDto.login())
                .password(passwordEncoder.encode(createUserDto.password()))
                .build();

        return userRepository.save(newUser);
    }

    public void setEmailConfirmed(SecurityUser securityUser) {
        var user = securityUser.user();
        user.setEmailConfirmed(true);
        userRepository.save(user);
    }

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .map(userMapper::map)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
