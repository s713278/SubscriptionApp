package com.app.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private CacheConfig cacheConfig;
    private Map<String,ProviderConfig> smsProviders;
    private SubscriptionConfig subscriptionConfig;
    
    @Getter
    @Setter
    public static class CustomerConfig {
        private Long otpExpTime;
        private Long emailTokenExp;
        private List<String> addressValidKeys;
        private Set<String> preferenceValidKeys;
        private boolean otpVerificationEnabled;
    }
    @Getter
    @Setter
    public static class SubscriptionConfig {
        private Set<String> eligibleDeliveryKeys;
    }

    @Getter
    @Setter
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
    public static class ProviderConfig{
        private String url;
        private String apiKey;
        private String apiSecret;
        private String route;
        private String flash;
        private String fromBrand;
    }

    @Setter
    @Getter
    public static class CacheConfig{
        private Long dbDataTTL;
        private long dbDataMaxSize;
        private Long otpTTL;
        private long otpMax;
    }
}
