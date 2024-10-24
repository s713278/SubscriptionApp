package com.app.notification.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.app.entites.Customer;
import com.app.entites.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Primary
@Qualifier("emailNotificationStrategy") public class EmailNotificationStrategy implements NotificationStrategy {

    private final EmailService emailService;
    @Override
    public void sendNotification(Customer customer, Order order) {
        // Logic to send email
        emailService.sendOrderNotification(customer.getEmail(),order);
    }
}
