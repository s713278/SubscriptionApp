package com.app.config;

import static org.junit.jupiter.api.Assertions.*;

import com.app.TestMockConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestMockConfig.class})
class GlobalConfigTest {

  @Autowired GlobalConfig globalConfig;

  @Test
  void getJwtConfig() {
    assertNotNull(globalConfig.getJwtConfig());
  }

  @Test
  void getCustomerConfig() {
    assertNotNull(globalConfig.getCustomerConfig());
  }

  @Test
  void getCacheConfig() {
    assertNotNull(globalConfig.getCacheConfig());
  }
}
