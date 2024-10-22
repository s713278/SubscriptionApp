package com.app.services;

import org.springframework.stereotype.Component;

import com.app.auth.services.OTPService;
import com.app.notification.services.EmailService;
import com.app.notification.services.SMSService;
import com.app.services.impl.SkuService;
import com.app.services.impl.VendorService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor
public class ServiceManager {
    private final SkuService skuService;
    private final VendorService vendorService;
    private final OTPService otpService;
    private final EmailService emailService;
    private final SMSService smsService;
}
