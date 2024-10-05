package com.app.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailService {

    public void sendOtp(String email, String otp) {
        // Logic to send email with OTP
        // You can use JavaMailSender or third-party services like SendGrid or AWS SES
    }
    
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Send activation email
    
    public void sendActivationEmail(String email, String activationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Account Activation");
        message.setText("Click the link to activate your account: http://localhost:8080/api/auth/activate/" + activationToken);
      try {
          mailSender.send(message);
      }catch(Exception e) {
          log.error("Unable to send user {}",email);
      }
    }

    // Send reset password email
    public void sendResetPasswordEmail(String email, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Password");
        message.setText("Click the link to reset your password: http://localhost:8080/api/auth/reset-password?token=" + resetToken);
        mailSender.send(message);
    }
}
