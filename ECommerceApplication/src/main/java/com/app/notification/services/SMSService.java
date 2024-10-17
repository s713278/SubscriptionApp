package com.app.notification.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.app.config.GlobalConfig;
import com.app.entites.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SMSService {

    private final GlobalConfig globalConfig;
    private final RestClient restClient = RestClient.create();

    private final ObjectMapper objectMapper;
    @Async
    public void sendOrderNotification(Long mobile, Order order) {
        log.info("User order :#{} notification will be sent to mobile number : {}",order.getOrderId(),mobile);
    }

    @Async
    public void sendOTP(Long mobileNo,String otp) {
        log.info("Is OTP sending and verification is enabled ? {} ",globalConfig.getCustomerConfig().isOtpVerificationEnabled());
        if(!globalConfig.getCustomerConfig().isOtpVerificationEnabled()){
            return;
        }
        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", globalConfig.getSmsApiConfig().getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Body (form data)
        Map<String, String> formData = new HashMap<>();
        formData.put("variables_values", String.valueOf(otp));
        formData.put("route", globalConfig.getSmsApiConfig().getRoute());
        formData.put("numbers", String.valueOf(mobileNo));
        formData.put("message","This is OTP :"+otp);
        formData.put("language","english");

        try {
            // Use RestClient to send POST request
            ResponseEntity<String> response = restClient
                    .post()
                    .uri(globalConfig.getSmsApiConfig().getUrl())
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
