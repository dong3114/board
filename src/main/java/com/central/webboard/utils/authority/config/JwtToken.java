package com.central.webboard.utils.authority.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtToken {
    private String secret;
    private String issuer;
    private long expiration;
    private String tokenPrefix;
}
