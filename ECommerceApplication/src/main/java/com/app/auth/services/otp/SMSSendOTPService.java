package com.app.auth.services.otp;

import org.springframework.stereotype.Component;

import com.app.payloads.request.OTPRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component(value = "smsOTPTemplate")
public class SMSSendOTPService extends AbstractSendOTPService {
    @Override
    protected void sendOtp(OTPRequest otpRequest, String otp) {
        serviceManager.getSmsService().sendTextMessage(""+otpRequest.mobile(),otp);
    }
}
