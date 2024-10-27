package com.app.services.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles({"dev"})
@Slf4j
class SMSServiceTest {

    @Autowired
    private SMSService smsService;
    @BeforeEach
    void setUp() {
    }

   // @Disabled
    @Test
    void sendOTP() {
        smsService.sendTextMessage("919912149049L","Welcome message from - SnR");
        smsService.sendTextMessage("12019206694L","Welcome message from - SnR");
    }
}