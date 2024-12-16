package com.app.services.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.app.CommonConfig;


@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {CommonConfig.class})
class SMSServiceTest  {

    @Autowired
    private SMSService smsService;
    @BeforeEach
    void setUp() {
    }

    @Disabled
    @Test
    void sendOTP() {
        smsService.sendTextMessage("+919912149049","Welcome message from - SnR");
       // smsService.sendTextMessage("12019206694L","Welcome message from - SnR");
    }
}