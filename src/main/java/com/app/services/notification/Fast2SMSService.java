package com.app.services.notification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.app.config.AppConstants;
import com.app.config.GlobalConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public  class Fast2SMSService extends SMSService {

    public Fast2SMSService(GlobalConfig globalConfig, ObjectMapper objectMapper) {
        super(globalConfig, objectMapper);
    }

    @Override
    @Async
    public void sendTextMessage(String mobileNo,String otp) {
        log.info("Is OTP sending and verification is enabled ? {} ",globalConfig.getCustomerConfig().isOtpVerificationEnabled());
        if(!globalConfig.getCustomerConfig().isOtpVerificationEnabled()){
            return;
        }
        var smsConfig = globalConfig.getSmsProviders().get(AppConstants.SMS_FAST2SMS_PROVIDER);
        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", smsConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Body (form data)
        Map<String, String> formData = new HashMap<>();
        formData.put("variables_values", String.valueOf(otp));
        formData.put("route", smsConfig.getRoute());
        formData.put("numbers", String.valueOf(91+mobileNo));
        formData.put("message","This is OTP :"+otp);
        formData.put("language","english");

        try {
            // Use RestClient to send POST request
            ResponseEntity<String> response = restClient
                    .post()
                    .uri(smsConfig.getUrl())
                    .headers(h -> h.addAll(headers))
                    .body(formData)
                    .retrieve()
                    .toEntity(String.class);
           log.info("JSON Response body {}",objectMapper.writeValueAsString(response.getBody()));
log.info(">>>>>>>>> Response {}",response.getStatusCode());
            if (response.getStatusCode() == HttpStatus.OK) {
              //  return response.getBody();
            } else {
              //  throw new RestClientException("Failed to send SMS, status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("OTP failed to send for mobile number : {}",mobileNo);
          //  throw new RestClientException("Exception occurred while sending SMS: " + e.getMessage(), e);
        }
    }
}
