package com.app.services;

import org.springframework.stereotype.Component;

import com.app.security.RefreshTokenService;
import com.app.security.TokenService;
import com.app.services.impl.SkuPriceService;
import com.app.services.impl.SkuService;
import com.app.services.impl.UserService;
import com.app.services.impl.VendorService;
import com.app.services.notification.NotificationContext;
import com.app.services.notification.OTPService;

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
    private final NotificationContext notificationContext;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    private final SubscriptionService subscriptionService;
    private final SkuSubscriptionService skuSubscriptionService;
    private final SkuPriceService priceService;
}
