package com.app.services.notification;

import org.springframework.scheduling.annotation.Async;

import com.app.entites.Order;

public interface NotificationStrategy {
    void sendOTP(String to , String otp);
    public void sendActivationEmail(String email, String activationToken) ;

    // Send reset password email
    public void sendResetPasswordEmail(String email, String resetToken) ;
    //We can apply retry mechanism
    @Async
    public void sendOrderNotification(String email, Order order);

}
