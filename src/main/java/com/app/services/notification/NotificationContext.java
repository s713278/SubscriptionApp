package com.app.services.notification;

import java.util.Arrays;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.app.constants.NotificationType;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class NotificationContext {
    private final Map<String, NotificationStrategy> notificationStrategies;

    private final OTPService otpService;
    @Async
    public void sendOTPMessage(NotificationType type,String to){
        log.debug("OTP is going to send to user/mobile :{}",to);
        var otp=otpService.generateOtp(to);
        log.debug("OTP is generated and Its :{} for user/mobile :{}",otp,to);
        switch (type){
            case SMS -> notificationStrategies.get("smsNotificationStrategy").sendOTP(to,otp);
            case EMAIL -> notificationStrategies.get("emailNotificationStrategy").sendOTP(to,otp);
            case WHATSAPP -> notificationStrategies.get("wNotificationStrategy").sendOTP(to,otp);
            case null, default -> throw new APIException(APIErrorCode.API_400,type +" should be one of the "+ Arrays.toString(NotificationType.values()));
        }
    }
    @Async
    public void sendOTPMessage(NotificationType type,String to,String otp){
         switch (type){
            case SMS -> notificationStrategies.get("smsNotificationStrategy").sendOTP(to,otp);
            case EMAIL -> notificationStrategies.get("emailNotificationStrategy").sendOTP(to,otp);
            case WHATSAPP -> notificationStrategies.get("wNotificationStrategy").sendOTP(to,otp);
             case null, default -> throw new APIException(APIErrorCode.API_400,type +" should be one of the "+ Arrays.toString(NotificationType.values()));
        }
    }
    @Async
    public void sendResetPasswordEmail(NotificationType type,String to, String resetToken){
        switch (type){
            case SMS -> notificationStrategies.get("smsNotificationStrategy").sendResetPasswordEmail(to,resetToken);
            case EMAIL -> notificationStrategies.get("emailNotificationStrategy").sendResetPasswordEmail(to,resetToken);
            case WHATSAPP -> notificationStrategies.get("wNotificationStrategy").sendResetPasswordEmail(to,resetToken);
            case null, default -> throw new APIException(APIErrorCode.API_400,type +" should be one of the "+ Arrays.toString(NotificationType.values()));
        }
    }
}
