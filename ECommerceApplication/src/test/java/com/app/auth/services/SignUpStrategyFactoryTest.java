package com.app.auth.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Profile("dev")
@Slf4j
class SignUpStrategyFactoryTest {

    @Autowired
    private SignUpStrategyFactory signUpStrategyFactory;

    @Test
    void testGetStrategy() {
    	
        assertAll(() -> {
            assertFalse(signUpStrategyFactory.getSignUpStrategies().isEmpty());
            assertEquals(3, signUpStrategyFactory.getSignUpStrategies().size());
            assertNotNull(signUpStrategyFactory.getStrategy("emailSignUpStrategy"));
            assertNotNull(signUpStrategyFactory.getStrategy("mobileSignUpStrategy"));
            assertNotNull(signUpStrategyFactory.getStrategy("mobileSignUpStrategy"));
        });
        signUpStrategyFactory.getSignUpStrategies().forEach((k,v)->System.out.println("Key :"+k+" \t Value :"+v));
    }

}
