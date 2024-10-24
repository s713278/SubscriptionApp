package com.app.notification.services;


import org.springframework.http.*;
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

    protected String otpMessage ="%s is your OTP for your account verification.Please verify!!";
    protected final GlobalConfig globalConfig;
    protected final RestClient restClient = RestClient.create();
    protected final ObjectMapper objectMapper;
    public void sendOrderNotification(String mobile, Order order) {
        log.info("User order :#{} notification will be sent to mobile number : {}",order.getId(),mobile);
    }
    public abstract void sendTextMessage(String to,String message) ;
}
