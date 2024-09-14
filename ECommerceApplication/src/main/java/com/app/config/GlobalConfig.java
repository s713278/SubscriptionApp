package com.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "application")
public class GlobalConfig {

    public static class CustomerConfig {
        private Long otpExpTime;

    }

    public static class VendorConfig {

    }
}
