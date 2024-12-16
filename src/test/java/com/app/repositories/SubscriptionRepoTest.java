package com.app.repositories;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.app.CommonConfig;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {CommonConfig.class})
class SubscriptionRepoTest {

    @Test
    void testFindVendorIdAndStatus() {
        fail("Not yet implemented");
    }

}
