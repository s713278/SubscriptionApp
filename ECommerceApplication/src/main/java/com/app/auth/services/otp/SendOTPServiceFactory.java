package com.app.auth.services.otp;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class SendOTPServiceFactory {

    private final Map<String, AbstractSendOTPService> otpMap ;

    public SendOTPServiceFactory(Map<String, AbstractSendOTPService> map){
        this.otpMap=map;
    }

}