package com.app.auth.services;

import org.springframework.stereotype.Component;

@Component
public class OTPService {

       public String generateOtp() {
            return String.valueOf((int) (Math.random() * 900000) + 100000); // Generates 6-digit OTP
        }
}
