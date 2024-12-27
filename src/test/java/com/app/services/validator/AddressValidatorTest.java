package com.app.services.validator;

import static org.junit.jupiter.api.Assertions.fail;

import com.app.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {TestContainerConfig.class})
class AddressValidatorTest {

  @Test
  void test() {
    fail("Not yet implemented");
  }
}
