package com.app.services;


import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.app.CommonConfig;
import com.app.config.AppConstants;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {CommonConfig.class})
@Slf4j
public class EmailOrMobileTest {

    // Regular expression to validate a mobile number (Indian mobile number format example)
    private static final Pattern MOBILE_REGEX = Pattern.compile(AppConstants.MOBILE_REGEX);
    // Updated regular expression to validate an email format, preventing consecutive dots anywhere in the domain
    private static final Pattern EMAIL_REGEX = Pattern.compile(AppConstants.EMAIL_REGEX);


    @Test
    void testMobileNumber() {
        String str = "9912149049"; // Change to an email to test email validation
        Assertions.assertTrue(MOBILE_REGEX.matcher(str).matches());
    }

    @Test
    void testInvalidMobileNumbers() {
        String[] invalidMobileNumbers = {
                "123",               // Too short
                "12345678901",       // Too long
                "99121abc49",        // Contains non-numeric characters
                "+919912149049",     // Country code included
                " 9912149049 "       // Leading/trailing spaces
        };

        for (String mobile : invalidMobileNumbers) {
            Assertions.assertFalse(MOBILE_REGEX.matcher(mobile).matches(), "Failed for mobile: " + mobile);
            log.info("Invalid mobile number correctly identified: {}", mobile);
        }
    }
    @Test
    void testValidEmailId() {
        String str = "swamy.kunta@gmail.com"; // Change to an email to test email validation
        Assertions.assertTrue(EMAIL_REGEX.matcher(str).matches());
        str = "swamy_KUNTA@gmail.com"; // Change to an email to test email validation
        Assertions.assertTrue(EMAIL_REGEX.matcher(str).matches());
    }

    @Test
    void testInvalidEmails() {
        String[] invalidEmails = {
                "example.com",       // Missing '@'
                "example@com",       // Missing domain extension
                "@example@agmail.....com",      // Missing local part
                "example@.com",      // Incomplete domain name
                "example@test..com", // Consecutive dots in domain
                "example@com."       // Domain ends with a dot
        };

        for (String email : invalidEmails) {
            Assertions.assertFalse(EMAIL_REGEX.matcher(email).matches(), "Failed for email: " + email);
            log.info("Invalid email correctly identified: {}", email);
        }
    }
}
