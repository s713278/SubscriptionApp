package com.app.event;

import com.app.services.notification.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailActivationListner {

  private final EmailService emailService;

  @Async
  @EventListener
  public void sendActivationLink(EmailActivationEvent event) {
    // Simulate sending an email
    log.info("Sending email to: {}", event.getEmail());
    emailService.sendActivationEmail(event.getEmail(), event.getEmailActivationtoken());
    // Actual email sending logic goes here
  }
}
