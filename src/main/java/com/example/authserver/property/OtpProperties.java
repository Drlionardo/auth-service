package com.example.authserver.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Configuration properties for OtpSerivce.
 *
 * @author Drlionardo
 * @see com.example.authserver.data.otp.OtpService
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "otp")
public class OtpProperties {
    private String length;

    private String validCharacters;

    @DurationUnit(ChronoUnit.HOURS)
    private Duration expirationTimeInHours;

}
