package com.app.event;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class CustomerSignUpEvent extends ApplicationEvent {

    /**
     * 
     */
    private static final long serialVersionUID = -6324921889128216197L;
    private String email;
    private String mobileNumber;
    private String otp;
    private String emailActivationtoken;

    public CustomerSignUpEvent(Object source, String email, String mobileNumber, String emailActivationtoken,String otp) {
        super(source);
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.emailActivationtoken = emailActivationtoken;
        this.otp=otp;
    }

  
}
