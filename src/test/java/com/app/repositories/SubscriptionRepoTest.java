package com.app.repositories;

import com.app.AbstractBaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.fail;

@Transactional
class SubscriptionRepoTest extends AbstractBaseConfig {

    @Test
    void testFindVendorIdAndStatus() {
        fail("Not yet implemented");
    }

}
