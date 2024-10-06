package com.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ConfigurationProperties(prefix = "application")
@Component
public class GlobalConfig {

    private JwtConfig jwtConfig;
    private CustomerConfig customerConfig;
    
    @Getter
    @Setter
    public static class CustomerConfig {
        private Long otpExpTime;
        private Long emailTokenExp;

    }

    public static class VendorConfig {

    }
    
    @Setter
    @Getter
    public static class JwtConfig {
        private String issuer;
        private String secret;
        private Long accessExpTime;
        private Long refreshExpTime;
    }
}
