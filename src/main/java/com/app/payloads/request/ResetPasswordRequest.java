package com.app.payloads.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
  private String token;
  private String newPassword;

  // Getters and setters
}
