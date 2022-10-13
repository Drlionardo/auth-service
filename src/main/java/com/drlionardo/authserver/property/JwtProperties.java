package com.drlionardo.authserver.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for JWT.
 *
 * @author Drlionardo
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * Private key for signing JWT with HMAC-SHA algorithms
     */
    private String signingKey;

    /**
     * Issuer for signed JWTs
     */
    private String issuer;

}
