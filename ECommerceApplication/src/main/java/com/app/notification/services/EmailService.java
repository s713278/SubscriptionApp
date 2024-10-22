package com.app.notification.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.app.entites.Order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    public void sendOtp(String email, String otp) {
        // Logic to send email with OTP
        // You can use JavaMailSender or third-party services like SendGrid or AWS SES
    }
    // Send activation email

    public void sendActivationEmail(String email, String activationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Account Activation");
        message.setText(
                "Click the link to activate your account: http://localhost:8080/api/auth/activate/" + activationToken);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Unable to send user {}", email);
        }
    }

    // Send reset password email
    public void sendResetPasswordEmail(String email, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Password");
        message.setText("Click the link to reset your password: http://localhost:8080/api/auth/reset-password?token="
                + resetToken);
        mailSender.send(message);
    }

    //We can apply retry mechanism
    @Async
    public void sendOrderNotification(String email, Order order) {
        String emailBody =  String.format("Order Created", "Your order # %s is created.",order.getId());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Order Confirmation");
        message.setText(
                emailBody);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Unable to send order email confirmation {}", email);
            
        }
        log.info("Email notification is sending to ::{}, with the message ::{}",email, "Order Created", "Your order #" + order.getId() + " is created.");
    }
}
