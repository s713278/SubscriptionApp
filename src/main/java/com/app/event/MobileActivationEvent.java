package com.app.event;
import java.io.Serial;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class MobileActivationEvent extends ApplicationEvent {

    /**
     * 
     */
    @Serial
    private static final long serialVersionUID = 6344488216487370912L;
    private final Long mobileNumber;
    private final String otp;

    public MobileActivationEvent(Object source,  Long mobileNumber,String otp) {
        super(source);
        this.mobileNumber = mobileNumber;
        this.otp=otp;
    }

}
