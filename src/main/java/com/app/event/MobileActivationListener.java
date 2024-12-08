package com.app.event;

import com.app.services.notification.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MobileActivationListener {

    private final SMSService smsService;
        @EventListener
        public void sendActivationLink(MobileActivationEvent event) {
            // Simulate sending an email
           log.info("Sending OTP : {}  to: {}" , event.getOtp(),event.getMobileNumber());
            // Actual email sending logic goes here
            smsService.sendTextMessage(String.valueOf(event.getMobileNumber()),event.getOtp());
        }
}
