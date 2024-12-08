package com.app.services;

import com.app.AbstractBaseConfig;
import com.app.services.impl.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j

@Transactional
class SkuServiceTest extends AbstractBaseConfig {


    @Autowired
    SkuService skuService;
    @Test
    void getPriceForSku() {
    }

    @Test
    void fetchVendorProductSkusById() {
        var vendorProductsMap = skuService.fetchProductSkusByVendorId(1L);
        //Assertions.assertEquals(2,vendorProductsMap.size());
    }
}