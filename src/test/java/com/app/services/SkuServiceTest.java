package com.app.services;

import com.app.config.TestContainerConfig;
import com.app.config.TestMockConfig;
import com.app.entites.ServiceAttribute;
import com.app.repositories.RepositoryManager;
import com.app.services.impl.DefaultSkuService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestContainerConfig.class, TestMockConfig.class})
class SkuServiceTest {

  @Autowired DefaultSkuService skuService;
  @Autowired RepositoryManager repositoryManager;

  @Test
  void getPriceForSku() {}

  @Test
  void fetchVendorProductSkusById() {
    var vendorProductsMap = skuService.fetchAllProductSkusByVendorId(1L);
    // Assertions.assertEquals(2,vendorProductsMap.size());
  }

  @DisplayName("Test Fetch service attributes method")
  @Test
  void testFetchServiceAttributesFindBySkuId() {
    Long skuId = 502L; // Replace with an existing Sku ID in your test DB
    Optional<ServiceAttribute> result =
        repositoryManager.getServiceAttributesRepo().findBySkuId(skuId);
    Assertions.assertTrue(result.isPresent());
  }
}
