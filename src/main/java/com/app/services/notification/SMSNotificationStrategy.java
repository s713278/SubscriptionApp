package com.app.services.notification;

import com.app.entites.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** SMS Service */
@Component(value = "smsNotificationStrategy")
@RequiredArgsConstructor
public class SMSNotificationStrategy implements NotificationStrategy {

  private final SMSService smsService;

  @Override
  public void sendOTP(String mobile, String otp) {
    smsService.sendTextMessage(mobile, otp);
  }

  @Override
  public void sendActivationEmail(String email, String activationToken) {}

  @Override
  public void sendResetPasswordEmail(String email, String resetToken) {}

  @Override
  public void sendOrderNotification(String email, Order order) {}
}
