package com.app.auth.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.services.cache.OTPCacheManager;

@Service
public class OTPService {

    private final Random random = new Random();

    @Autowired
    private OTPCacheManager otpCacheManager; // Reference to the cache manager

    private static final int MAX_ATTEMPTS = 3; // Maximum attempts
    private static final int ATTEMPT_EXPIRY_MINUTES = 5; // Attempts expire after 5 minutes

    public String generateOtp(String mobileNumber) {
        String otp = String.format("%06d", random.nextInt(1000000)); // Generate a 6-digit OTP
        otpCacheManager.putOtp(mobileNumber, otp); // Store the OTP in the cache
        otpCacheManager.resetAttempts(mobileNumber); // Reset attempts on successful OTP generation
        return otp; // Return the generated OTP
    }

    @Cacheable(value = "otpCache", key = "#mobileNumber")
    public String getOtp(String mobileNumber) {
        return generateOtp(mobileNumber); // If OTP is not found, this method will return null
    }

    public void verifyOtp(String mobileNumber, String otp) {
        String cachedOtp = getOtp(mobileNumber);
        int attempts = otpCacheManager.getAttempts(mobileNumber);

        if (attempts >= MAX_ATTEMPTS) {
            throw new APIException(APIErrorCode.API_429, "Maximum attempts exceeded. Please try again later.");
        }

        if (cachedOtp != null && cachedOtp.equals(otp)) {
            // OTP is valid; reset attempts on success
            otpCacheManager.evictAttempts(mobileNumber);
        } else {
            // Increment the attempt count
            otpCacheManager.incrementAttempts(mobileNumber);
            throw new APIException(APIErrorCode.API_401, "OTP Invalid or Expired");
        }
    }
}
