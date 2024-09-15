package com.app.event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditLogListener {

    @Async
    @EventListener
    public void handleUserSignUp(CustomerSignUpEvent event) {
        // Simulate logging user registration event
       log.debug("Logging audit for user: " + event.getEmail());
        // Actual audit logging logic goes here
    }
}
