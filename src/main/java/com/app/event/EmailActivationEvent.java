package com.app.event;

import java.io.Serial;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailActivationEvent extends ApplicationEvent {

  /** */
  @Serial private static final long serialVersionUID = -6324921889128216197L;

  private final String email;
  private final String otp;
  private final String emailActivationtoken;

  public EmailActivationEvent(
      Object source, String email, String emailActivationtoken, String otp) {
    super(source);
    this.email = email;
    this.emailActivationtoken = emailActivationtoken;
    this.otp = otp;
  }
}
