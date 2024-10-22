package com.app.services.cache;

import java.util.List;
import java.util.Objects;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.app.constants.CacheType;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class OTPCacheManager {

   private  final CacheManager cacheManager;


   private Cache getCache(final String name){
     return  cacheManager.getCache(name);
   }

   public List<String> getCacheNames(){
       return cacheManager.getCacheNames().stream().toList();
   }

   @PostConstruct
   void checkCacheManager(){
       Objects.requireNonNull(cacheManager);
   }
    public void putOtp(String key, String otp) {
        getCache(CacheType.CACHE_TYPE_OTP).put(key, otp);
    }

    public String getOtp(String key) {
       return getCache(CacheType.CACHE_TYPE_OTP).get(key, String.class);
    }

    public void evictOtp(String key) {
        getCache(CacheType.CACHE_TYPE_OTP).evict(key);
    }

    // Methods for managing attempts
    public void incrementAttempts(String key) {
        Integer attempts = getAttempts(key);
        attempts = (attempts == null) ? 1 : attempts + 1;
        cacheManager.getCache("attemptsCache").put(key, attempts);
    }

    public void resetAttempts(String key) {
        Objects.requireNonNull(cacheManager.getCache("attemptsCache")).evict(key);
    }

    public void evictAttempts(String key) {
        Objects.requireNonNull(cacheManager.getCache("attemptsCache")).evict(key);
    }

    public Integer getAttempts(String key) {
        return Objects.requireNonNull(cacheManager.getCache("attemptsCache")).get(key, Integer.class);
    }
}
