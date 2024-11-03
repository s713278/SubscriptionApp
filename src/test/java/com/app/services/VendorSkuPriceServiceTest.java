package com.app.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.app.AbstractBaseConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@Transactional
class VendorSkuPriceServiceTest extends AbstractBaseConfig {

    @Autowired
    VendorSkuPriceService vendorSkuPriceService;
    @Test
    void getPriceForSku() {
    }

    @Test
    void fetchVendorProductsById() {
        var vendorProductsMap = vendorSkuPriceService.fetchProductsByVendorId(3L);
        Assertions.assertEquals(2,vendorProductsMap.size());
    }
}