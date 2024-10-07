package com.app.repositories;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor // Constructor Injection
public class RepositoryManager {

    private final SubscriptionRepository subscriptionRepo;
    private final CustomerRepo customerRepo;
    private final VendorRepo vendorRepo;
    private final SkuRepo skuRepo;
    private final VendorSkuPriceRepo vendorSkuPriceRepo;
    private final RoleRepo roleRepo;
}
