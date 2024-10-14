package com.app.notification.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.app.entites.Order;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SMSService {

    
    @Async
    public void sendOrderNotification(Long mobile, Order order) {

        log.info("SMS notification is sending to ::{}, with the message ::{}",mobile, "Order Created", "Your order #" + order.getOrderId() + " is created.");
    }
}
