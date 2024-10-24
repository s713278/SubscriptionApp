package com.app.auth.services;

import java.util.Random;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.app.constants.CacheType;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.services.cache.OTPCacheManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OTPService {

    private final Random random = new Random();

    private final OTPCacheManager otpCacheManager;

    private static final int MAX_ATTEMPTS = 3; // Maximum attempts
    private static final int ATTEMPT_EXPIRY_MINUTES = 5; // Attempts expire after 5 minutes

    private String getRandomNumber(){
        return  String.format("%06d", random.nextInt(1000000));
    }

    @Cacheable(value = CacheType.CACHE_TYPE_OTP,key = "#key")
    public String generateOtp(String key) {
       // Generate a 6-digit OTP
        return getRandomNumber(); // Return the generated OTP
    }

    public void verifyOtp(final String key, final String otp) {
        String cachedOtp = otpCacheManager.getOtp(key);
        if(cachedOtp == null){
            throw new APIException(APIErrorCode.API_401, "OTP is already expired!!");
        }
        if(!cachedOtp.equals(otp)){
            throw new APIException(APIErrorCode.API_401, "Invalid OTP!!");
        }
        otpCacheManager.evictOtp(key);
       /* int attempts = otpCacheManager.getAttempts(userId);

        if (attempts >= MAX_ATTEMPTS) {
            throw new APIException(APIErrorCode.API_429, "Maximum attempts exceeded. Please try again later.");
        }

        if (cachedOtp.equals(otp)) {
            // OTP is valid; reset attempts on success
            otpCacheManager.resetAttempts(userId);
            otpCacheManager.evictAttempts(userId);
        } else {
            // Increment the attempt count
            otpCacheManager.incrementAttempts(userId);
            throw new APIException(APIErrorCode.API_401, "OTP Invalid or Expired");
        }*/
    }
}
