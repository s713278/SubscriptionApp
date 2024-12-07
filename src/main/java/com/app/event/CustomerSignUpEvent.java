package com.app.event;
import java.io.Serial;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class CustomerSignUpEvent extends ApplicationEvent {

    /**
     * 
     */
    @Serial
    private static final long serialVersionUID = -6324921889128216197L;
    private String email;
    private Long mobileNumber;
    private final String otp;
    private String emailActivationtoken;

    public CustomerSignUpEvent(Object source, String email, String emailActivationtoken,String otp) {
        super(source);
        this.email = email;
        this.emailActivationtoken = emailActivationtoken;
        this.otp=otp;
    }

    public CustomerSignUpEvent(Object source,  Long mobileNumber,String otp) {
        super(source);
        this.mobileNumber = mobileNumber;
        this.otp=otp;
    }

}
