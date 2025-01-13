package com.app.services.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.app.config.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {TestContainerConfig.class})
class OTPCacheManagerTest {

  @Autowired OTPCacheManager otpCacheManager;

  @Test
  void getCacheNames() {
    assertNotNull(otpCacheManager);
    assertEquals(4, otpCacheManager.getCacheNames().size());
  }
}
