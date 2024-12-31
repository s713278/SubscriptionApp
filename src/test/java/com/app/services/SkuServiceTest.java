package com.app.services;

import com.app.TestContainerConfig;
import com.app.services.impl.DefaultSkuService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {TestContainerConfig.class})
class SkuServiceTest {

  @Autowired DefaultSkuService skuService;

  @Test
  void getPriceForSku() {}

  @Test
  void fetchVendorProductSkusById() {
    var vendorProductsMap = skuService.fetchProductSkusByVendorId(1L);
    // Assertions.assertEquals(2,vendorProductsMap.size());
  }
}
