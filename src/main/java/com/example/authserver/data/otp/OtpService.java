package com.example.authserver.data.otp;

import com.example.authserver.data.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class OtpService {
    private final OtpRepository otpRepository;
    private final UserService userService;
    private final SecureRandom random = new SecureRandom();

    private final String VALID_CHARACTERS;
    private final int CODE_LENGTH;
    private final Duration EXPIRATION_DURATION;

    public OtpService(OtpRepository otpRepository, UserService userService,
                      @Value("${otp.valid-characters}") String validCharacters,
                      @Value("${otp.length}") int codeLength,
                      @Value("${otp.expiration-time-in-hours}") Duration EXPIRATION_DURATION) {
        this.otpRepository = otpRepository;
        this.userService = userService;
        this.VALID_CHARACTERS = validCharacters;
        this.CODE_LENGTH = codeLength;
        this.EXPIRATION_DURATION = EXPIRATION_DURATION;
    }

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
        //todo: sendCodeHere
    }

    private Otp createOtp(Long userId) {
        var otp = Otp.builder()
                .userId(userId)
                .code(generateOTP())
                .expiresAt(Instant.now().minus(EXPIRATION_DURATION))
                .build();
        return otpRepository.save(otp);
    }

    private String generateOTP() {
        return random.ints(CODE_LENGTH)
                .boxed()
                .map(index -> VALID_CHARACTERS.charAt(Math.abs(index) % VALID_CHARACTERS.length()))
                .map(Object::toString)
                .collect(Collectors.joining());
    }

}
