package com.app.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@AllArgsConstructor
public class CacheConfig {

    private final GlobalConfig globalConfig;

   // @Bean
    public CaffeineCacheManager dbCache() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("skus","vendors");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(globalConfig.getCacheConfig().getDbDataTTL(), TimeUnit.MINUTES)  // Set TTL to 10 minutes
                .maximumSize(globalConfig.getCacheConfig().getOtpMax()));                      // Maximum cache size
        return cacheManager;
    }

    //@Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("otpCache", "attemptsCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(globalConfig.getCacheConfig().getOtpTTL(), TimeUnit.MINUTES) // OTP and attempts expire after 5 minutes
                .maximumSize(globalConfig.getCacheConfig().getOtpMax())); // Maximum cache size
        return cacheManager;
    }

}
