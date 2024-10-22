package com.app.auth.services.otp;

import org.springframework.stereotype.Component;

import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.OTPRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SendOTPStrategy {

    private final SendOTPServiceFactory otpServiceFactory;
    public void sendOTP(OTPRequest otpRequest){
        if(otpRequest.mobile() !=null){
            otpServiceFactory.getOtpMap().get("smsOTPTemplate").processSendOTP(otpRequest);
        }else if(otpRequest.email() !=null){
            otpServiceFactory.getOtpMap().get("emailOTPTemplate").processSendOTP(otpRequest);
        }else{
            throw new APIException(APIErrorCode.API_400,"Un supported OTPRequest...");
        }
    }

}
