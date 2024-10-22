package com.app.auth.services.otp;


import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.app.payloads.request.OTPRequest;
import com.app.services.ServiceManager;
import com.app.services.cache.OTPCacheManager;

public abstract class AbstractSendOTPService {

    @Autowired
    protected ServiceManager serviceManager;
    private OTPCacheManager otpCacheManager; // Reference to the cache manager

    public void processSendOTP(OTPRequest otpRequest) {
        validate(otpRequest);
        String otp = generateOtp();
        sendOtp(otpRequest, otp);
        logOtpSend(otpRequest, otp);
    }

    @Cacheable(value = "otpCache", key = "#mobileNumber")
    protected  String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }


    protected abstract void sendOtp(OTPRequest otpRequest, String otp);

    //TODO: Needs OTP validation
    protected void validate(OTPRequest otpRequest) {
        // Implement common validation logic here
        // Check mobile number exists
        // Check no of attempts
    }

    private void logOtpSend(OTPRequest otpRequest, String otp) {
        // Logging logic
        // Update user status
    }
}
