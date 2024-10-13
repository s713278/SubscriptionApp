package com.app.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class SubscriptionRepoTest {

    @Test
    void testFindVendorIdAndStatus() {
        fail("Not yet implemented");
    }

}
