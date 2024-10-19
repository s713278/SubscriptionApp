package com.app.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.AllArgsConstructor;

@Configuration
@EnableCaching
@AllArgsConstructor
public class CacheConfig {

    private final GlobalConfig globalConfig;

    @Primary
    @Bean
    public CaffeineCacheManager dbCache() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("skus","vendors");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(globalConfig.getCacheConfig().getDbDataTTL(), TimeUnit.MINUTES)  // Set TTL to 10 minutes
                .maximumSize(globalConfig.getCacheConfig().getOtpMax()));                      // Maximum cache size
        return cacheManager;
    }


    @Qualifier("cacheManager") public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("otpCache", "attemptsCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(globalConfig.getCacheConfig().getOtpTTL(), TimeUnit.MINUTES) // OTP and attempts expire after 5 minutes
                .maximumSize(globalConfig.getCacheConfig().getOtpMax())); // Maximum cache size
        return cacheManager;
    }
}
