package com.app.event;

import com.app.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MobileActivationListener {

    private final EmailService emailService;
       @Async
        @EventListener
        public void sendActivationLink(MobileActivationEvent event) {
            // Simulate sending an email
           log.info("Sending OTP to: {}" , event.getMobileNumber());
            // Actual email sending logic goes here
        }
}
