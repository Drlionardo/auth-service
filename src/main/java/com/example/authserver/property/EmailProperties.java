package com.example.authserver.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Mailgun service API.
 *
 * @author Drlionardo
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    /**
     * Set from field in EmailService
     *
     * @see com.example.authserver.service.EmailService
     */
    private String from;
}
