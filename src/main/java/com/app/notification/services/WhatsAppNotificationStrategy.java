package com.app.notification.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.app.entites.Order;

import lombok.RequiredArgsConstructor;

/**
 * SMS Service 
 */
@Component
@RequiredArgsConstructor
@Qualifier("wNotificationStrategy") public class WhatsAppNotificationStrategy implements NotificationStrategy {

    private final SMSService smsService;

    @Override
    public void sendOTP( String mobile, String otp) {
        smsService.sendTextMessage(mobile,otp);
    }

    @Override
    public void sendActivationEmail(String email, String activationToken) {

    }

    @Override
    public void sendResetPasswordEmail(String email, String resetToken) {

    }

    @Override
    public void sendOrderNotification(String email, Order order) {

    }

}
