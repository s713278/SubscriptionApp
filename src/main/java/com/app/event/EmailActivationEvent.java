package com.app.event;
import java.io.Serial;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class EmailActivationEvent extends ApplicationEvent {

    /**
     * 
     */
    @Serial
    private static final long serialVersionUID = -6324921889128216197L;
    private String email;
    private String otp;
    private String emailActivationtoken;

    public EmailActivationEvent(Object source, String email, String emailActivationtoken,String otp) {
        super(source);
        this.email = email;
        this.emailActivationtoken = emailActivationtoken;
        this.otp=otp;
    }

}
