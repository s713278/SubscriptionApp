package com.app.event;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SMSNotificationListener {

    @Async
    @EventListener
    public void handleUserSignUp(CustomerSignUpEvent event) {
        // Simulate sending SMS
       log.info("Sending OTP SMS to: {}" , event.getMobileNumber());
        // Actual SMS sending logic goes here
    }
}
