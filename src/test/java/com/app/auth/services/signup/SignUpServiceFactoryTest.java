package com.app.auth.services.signup;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.app.services.auth.signup.SignUpServiceFactory;

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