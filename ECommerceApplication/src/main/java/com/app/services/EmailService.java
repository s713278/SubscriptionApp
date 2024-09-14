package com.app.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendOtp(String email, String otp) {
        // Logic to send email with OTP
        // You can use JavaMailSender or third-party services like SendGrid or AWS SES
    }
}
