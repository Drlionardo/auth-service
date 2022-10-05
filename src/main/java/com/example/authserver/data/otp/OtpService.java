package com.example.authserver.data.otp;

import com.example.authserver.data.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final UserService userService;
    private final SecureRandom random = new SecureRandom();

    private final String VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int CODE_LENGTH = 6;
    private final Duration expirationDuration = Duration.ofHours(24);

    @Transactional
    public boolean checkOtp(String email, String code) {
        var user = userService.getUserByEmail(email);
        var otpFromDb = otpRepository.findByUserIdAndCodeAndCreationDate(user.getId(),
                code, Instant.now().minus(expirationDuration));
        if (otpFromDb.isPresent()) {
            otpRepository.delete(otpFromDb.get());
            return true;
        } else {
            return false;
        }
    }

    public void sendOtpToEmail(Long userId, String email) {
        var otp = createOtp(userId);
        //todo: sendCodeHere
    }

    private Otp createOtp(Long userId) {
        var otp = Otp.builder()
                .userId(userId)
                .code(generateOTP())
                .creationTime(Instant.now())
                .build();
        return otpRepository.save(otp);
    }

    private String generateOTP() {
        return random.ints(CODE_LENGTH)
                .boxed()
                .map(index -> VALID_CHARACTERS.charAt(index % VALID_CHARACTERS.length()))
                .map(Object::toString)
                .collect(Collectors.joining());
    }

}
