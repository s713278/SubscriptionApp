package com.app.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificationListner {

       @Async
        @EventListener
        public void handleCustomerSignUp(CustomerSignUpEvent event) {
            // Simulate sending an email
           log.info("Sending email to: {}" , event.getEmail());
            // Actual email sending logic goes here
        }
}
