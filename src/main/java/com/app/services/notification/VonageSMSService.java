package com.app.services.notification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.app.config.AppConstants;
import com.app.config.GlobalConfig;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Primary
@Slf4j
@Component
public class VonageSMSService extends SMSService{
    public VonageSMSService(GlobalConfig globalConfig, ObjectMapper objectMapper) {
        super(globalConfig, objectMapper);
    }

    @Async
    @Override
    public void sendTextMessage(String mobileNo, String message) {
        log.debug("Sending OTP to mobileNo : {}",mobileNo);
        log.info("Is OTP sending and verification is enabled ? {} ",globalConfig.getCustomerConfig().isOtpVerificationEnabled());
        if(!globalConfig.getCustomerConfig().isOtpVerificationEnabled()){
            return;
        }
        var smsConfig=globalConfig.getSmsProviders().get(AppConstants.SMS_VONAGE_PROVIDER);
        // Headers
        // Body (form data)
        Map<String, String> formData = new HashMap<>();
        formData.put("from", smsConfig.getFromBrand());
        formData.put("to", mobileNo);
        formData.put("text",String.format(otpMessage,message));
        formData.put("api_key",smsConfig.getApiKey());
        formData.put("api_secret",smsConfig.getApiSecret());
        try {
            // Use RestClient to send POST request
            ResponseEntity<String> response = restClient
                    .post()
                    .uri(smsConfig.getUrl())
                   // .headers(h -> h.addAll(headers))
                    .body(formData)
                    .retrieve()
                    .toEntity(String.class);
            log.info("JSON Response body {}",objectMapper.writeValueAsString(response.getBody()));
            log.info(">>>>>>>>> Response {}",response.getStatusCode());
        } catch (Exception e) {
            log.error("OTP failed to send for mobile number : {}",mobileNo);
            throw new APIException(APIErrorCode.API_500,"Unable to send OTP " +mobileNo);
        }
    }
}
