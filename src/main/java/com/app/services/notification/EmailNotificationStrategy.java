package com.app.services.notification;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.app.entites.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Primary
@Qualifier("emailNotificationStrategy") public class EmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;


    @Override
    public void sendOTP(String email, String otp) {

    }

    public void sendActivationEmail(String email, String activationToken) {
        emailService.sendActivationEmail(email,activationToken);
    }

    // Send reset password email
    public void sendResetPasswordEmail(String email, String resetToken) {
        emailService.sendResetPasswordEmail(email,resetToken);
    }

    //We can apply retry mechanism
    @Async
    public void sendOrderNotification(String email, Order order) {
        emailService.sendOrderNotification(email,order);
          }

}
