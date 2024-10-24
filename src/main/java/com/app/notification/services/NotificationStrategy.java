package com.app.notification.services;

import com.app.entites.Customer;
import com.app.entites.Order;

public interface NotificationStrategy {
    void sendNotification(Customer customer, Order order);
}
