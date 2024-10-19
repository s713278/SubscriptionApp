package com.app.services.cache;

import java.util.Objects;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class OTPCacheManager {

    private final CacheManager cacheManager;

    public OTPCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void putOtp(String mobileNumber, String otp) {
        Objects.requireNonNull(cacheManager.getCache("otpCache")).put(mobileNumber, otp);
    }

    public String getOtp(String mobileNumber) {
        return Objects.requireNonNull(cacheManager.getCache("otpCache")).get(mobileNumber, String.class);
    }

    public void evictOtp(String mobileNumber) {
        Objects.requireNonNull(cacheManager.getCache("otpCache")).evict(mobileNumber);
    }

    // Methods for managing attempts
    public void incrementAttempts(String mobileNumber) {
        Integer attempts = getAttempts(mobileNumber);
        attempts = (attempts == null) ? 1 : attempts + 1;
        cacheManager.getCache("attemptsCache").put(mobileNumber, attempts);
    }

    public void resetAttempts(String mobileNumber) {
        Objects.requireNonNull(cacheManager.getCache("attemptsCache")).evict(mobileNumber);
    }

    public void evictAttempts(String mobileNumber) {
        Objects.requireNonNull(cacheManager.getCache("attemptsCache")).evict(mobileNumber);
    }

    public Integer getAttempts(String mobileNumber) {
        return Objects.requireNonNull(cacheManager.getCache("attemptsCache")).get(mobileNumber, Integer.class);
    }
}
