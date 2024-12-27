package com.app.services.notification;

import com.app.entites.Order;
import org.springframework.scheduling.annotation.Async;

public interface NotificationStrategy {
  void sendOTP(String to, String otp);

  void sendActivationEmail(String email, String activationToken);

  // Send reset password email
  void sendResetPasswordEmail(String email, String resetToken);

  // We can apply retry mechanism
  @Async
  void sendOrderNotification(String email, Order order);
}
