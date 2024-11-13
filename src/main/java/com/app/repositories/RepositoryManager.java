package com.app.repositories;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor // Constructor Injection
public class RepositoryManager {
    private final SubscriptionRepo subscriptionRepo;
    private final CustomerRepo customerRepo;
    private final VendorRepo vendorRepo;
    private final SkuRepo skuRepo;
    private final RoleRepo roleRepo;
    private final OrderRepo orderRepo;
}
