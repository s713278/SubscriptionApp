package com.app.notification.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.app.entites.Customer;
import com.app.entites.Order;

import lombok.RequiredArgsConstructor;
/**
 * SMS Service 
 */
@Component
@RequiredArgsConstructor
@Qualifier("smsNotificationStrategy") public class SMSNotificationStrategy implements NotificationStrategy {

    private final SMSService smsService;
    @Override
    public void sendNotification(Customer customer, Order order) {
        // Logic to send SMS
        smsService.sendOrderNotification(String.valueOf(customer.getMobile()), order);
    }
}
