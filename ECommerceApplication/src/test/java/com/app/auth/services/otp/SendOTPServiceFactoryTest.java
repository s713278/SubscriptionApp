package com.app.auth.services.otp;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class SendOTPServiceFactoryTest {

   @Autowired
   SendOTPServiceFactory otpFactory;
    @Test
    void testOTPFactoryInitialization() {
       assertFalse(otpFactory.getOtpMap().isEmpty());
        assertEquals(2, otpFactory.getOtpMap().size());
        assertTrue(otpFactory.getOtpMap().containsKey("smsOTPTemplate") &&
                otpFactory.getOtpMap().containsKey("emailOTPTemplate"));
    }
}