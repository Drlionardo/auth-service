package com.example.authserver.data.otp;

import com.example.authserver.data.user.UserService;
import com.example.authserver.property.OtpProperties;
import com.example.authserver.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final OtpProperties otpProperties;
    private final SecureRandom random = new SecureRandom();

    @Transactional
    public boolean checkOtp(String email, String code) {
        var user = userService.getUserByEmail(email);
        var otpFromDb = otpRepository.findByUserIdAndCodeAndExpiresAtBefore(user.getId(),
                code, Instant.now());
        if (otpFromDb.isPresent()) {
            otpRepository.delete(otpFromDb.get());
            return true;
        } else {
            return false;
        }
    }

    public void sendOtpToEmail(Long userId, String email) {
        var otp = createOtp(userId);
        emailService.sendSimpleMessage(email, "One time password for registration", otp.getCode());
    }

    private Otp createOtp(Long userId) {
        var otp = Otp.builder()
                .userId(userId)
                .code(generateOTP())
                .expiresAt(Instant.now().minus(otpProperties.getExpirationTimeInHours()))
                .build();
        return otpRepository.save(otp);
    }

    private String generateOTP() {
        String validCharacters = otpProperties.getValidCharacters();
        return random.ints(otpProperties.getLength())
                .boxed()
                .map(index -> validCharacters.charAt(Math.abs(index) % validCharacters.length()))
                .map(Object::toString)
                .collect(Collectors.joining());
    }

}
