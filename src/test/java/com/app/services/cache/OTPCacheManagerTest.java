package com.app.services.cache;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
@SpringBootTest
@ActiveProfiles("dev")
class OTPCacheManagerTest {

    @Autowired
    OTPCacheManager otpCacheManager;
    @Test
    void getCacheNames() {
       assertNotNull(otpCacheManager);
        assertEquals(4,otpCacheManager.getCacheNames().size());
    }
}