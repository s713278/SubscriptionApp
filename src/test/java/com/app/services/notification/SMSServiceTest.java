package com.app.services.notification;

import com.app.AbstractBaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class SMSServiceTest extends AbstractBaseConfig {

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