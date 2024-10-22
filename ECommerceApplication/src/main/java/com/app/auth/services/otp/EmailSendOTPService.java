package com.app.auth.services.otp;

import org.springframework.stereotype.Service;

import com.app.payloads.request.OTPRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service(value = "emailOTPTemplate")
public class EmailSendOTPService extends AbstractSendOTPService {
    @Override
    protected void sendOtp(OTPRequest otpRequest, String otp) {
        serviceManager.getEmailService().sendOtp(otpRequest.email(),otp);
    }
}
