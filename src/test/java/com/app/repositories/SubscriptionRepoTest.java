package com.app.repositories;

import static org.junit.jupiter.api.Assertions.fail;

import com.app.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {TestContainerConfig.class})
class SubscriptionRepoTest {

  @Test
  void testFindVendorIdAndStatus() {
    fail("Not yet implemented");
  }
}
