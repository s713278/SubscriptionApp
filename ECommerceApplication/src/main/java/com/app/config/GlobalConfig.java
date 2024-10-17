package com.app.config;

import java.util.List;

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
    private SmsApiConfig smsApiConfig;
    
    @Getter
    @Setter
    public static class CustomerConfig {
        private Long otpExpTime;
        private Long emailTokenExp;
        private List<String> addressValidKeys;
        private boolean otpVerificationEnabled;
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

    @Setter
    @Getter
    public static class SmsApiConfig{
        private String apiKey;
        private String route;
        private String url;
        private String flash;
    }
}
