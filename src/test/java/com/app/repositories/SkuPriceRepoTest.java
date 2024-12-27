package com.app.repositories;

import com.app.TestContainerConfig;
import com.app.entites.Sku;
import com.app.entites.type.SkuType;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {TestContainerConfig.class})
class SkuPriceRepoTest {

  @Autowired private RepositoryManager repositoryManager;

  @BeforeAll
  static void setUp() {
    Sku sku = new Sku();
    sku.setSkuType(SkuType.ITEM);
  }

  @Test
  void findTodayPriceBySku() {
    var today = LocalDate.now();
    var skuPriceEntity =
        repositoryManager.getPriceRepository().findTodayPriceBySku(10004L, LocalDate.now());
    Assertions.assertTrue(skuPriceEntity.isPresent());
    Assertions.assertEquals(
        (skuPriceEntity.get().getEffectiveDate().isBefore(today)
            || skuPriceEntity.get().getEffectiveDate().isEqual(today)),
        skuPriceEntity.get().getListPrice().doubleValue());
    // Assertions.assertEquals(90d,skuPriceEntity.get().getSalePrice().doubleValue());
  }

  @Test
  void findFuturePricesBySku() {}

  @Test
  void findPreviousPricesBySku() {}

  @Test
  void findFuturePriceBySku() {
    var skuPriceEntity =
        repositoryManager
            .getPriceRepository()
            .findFuturePriceBySku(10005L, LocalDate.of(2024, 12, 30));
    Assertions.assertTrue(skuPriceEntity.isPresent());
    Assertions.assertEquals(2800, skuPriceEntity.get().getListPrice().doubleValue());
    Assertions.assertEquals(2300, skuPriceEntity.get().getSalePrice().doubleValue());
  }
}
