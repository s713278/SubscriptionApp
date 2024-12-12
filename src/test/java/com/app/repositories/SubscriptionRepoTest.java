package com.app.repositories;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import com.app.AbstractBaseConfig;

@Transactional
class SubscriptionRepoTest extends AbstractBaseConfig {

    @Test
    void testFindVendorIdAndStatus() {
        fail("Not yet implemented");
    }

}
