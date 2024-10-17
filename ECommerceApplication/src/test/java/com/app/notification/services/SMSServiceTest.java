package com.app.notification.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Profile("dev")
@Slf4j
class SMSServiceTest {

    @Autowired
    private SMSService smsService;
    @BeforeEach
    void setUp() {
    }

    @Disabled
    @Test
    void sendOTP() {
        smsService.sendOTP(9912149049L,"909090");
    }
}