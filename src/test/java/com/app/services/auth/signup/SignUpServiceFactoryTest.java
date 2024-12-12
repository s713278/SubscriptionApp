package com.app.services.auth.signup;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class SignUpServiceFactoryTest {
private @Autowired SignUpServiceFactory signUpServiceFactory;
    @Test
    void testSingUpServiceInitialization() {
        assertAll(()->{
            var map =signUpServiceFactory.getMap();
            assertFalse(map.isEmpty());
            assertEquals(2, map.size());
            assertNotNull(map.get("emailSignUpService"));
            assertNotNull(map.get("mobileSignUpService"));
        });

    }
}