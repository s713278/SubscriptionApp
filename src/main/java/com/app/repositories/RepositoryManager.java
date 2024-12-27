package com.app.repositories;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
  private final SkuPriceRepo priceRepository;
  private final SkuSubscriptionRepo skuSubscriptionRepo;
  private final CategoryRepo categoryRepo;
  private final ProductRepo productRepo;
  private final VendorLegalDetailsRepo vendorLegalDetailsRepo;
}
