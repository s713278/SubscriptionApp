package com.app.event;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class MobileActivationEvent extends ApplicationEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 6344488216487370912L;
    private Long mobileNumber;
    private String otp;

    public MobileActivationEvent(Object source,  Long mobileNumber,String otp) {
        super(source);
        this.mobileNumber = mobileNumber;
        this.otp=otp;
    }

}
