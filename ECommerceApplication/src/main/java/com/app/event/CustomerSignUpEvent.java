package com.app.event;
import org.springframework.context.ApplicationEvent;

public class CustomerSignUpEvent extends ApplicationEvent {

    /**
     * 
     */
    private static final long serialVersionUID = -6324921889128216197L;
    private String email;
    private String mobileNumber;
    private String firstName;
    private String otp;

    public CustomerSignUpEvent(Object source, String email, String mobileNumber, String firstName,String otp) {
        super(source);
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.firstName = firstName;
        this.otp=otp;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getOtp() {
        return otp;
    }
}
