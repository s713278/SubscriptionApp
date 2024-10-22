package com.app.notification.services;


import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.app.config.GlobalConfig;
import com.app.entites.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public abstract class SMSService {

    protected final GlobalConfig globalConfig;
    protected final RestClient restClient = RestClient.create();
    protected final ObjectMapper objectMapper;
    @Async
    public void sendOrderNotification(String mobile, Order order) {
        log.info("User order :#{} notification will be sent to mobile number : {}",order.getId(),mobile);
    }
    @Async
    public abstract void sendTextMessage(String mobileNo,String message) ;
}
