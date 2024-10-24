package com.app.services;

import org.springframework.stereotype.Component;

import com.app.notification.services.NotificationService;
import com.app.notification.services.OTPService;
import com.app.services.impl.SkuService;
import com.app.services.impl.UserService;
import com.app.services.impl.VendorService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor
public class ServiceManager {
    private final UserService userService;
    private final SkuService skuService;
    private final VendorService vendorService;
    private final OTPService otpService;
    private final NotificationService notificationService;
}
