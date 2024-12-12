package com.app.repositories;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.app.AbstractBaseConfig;

@Transactional
class SkuPriceRepositoryTest extends AbstractBaseConfig {

    @Autowired
    private RepositoryManager repositoryManager;

    @Test
    void findTodayPriceBySku() {
        var skuPriceEntity =repositoryManager.getPriceRepository().findTodayPriceBySku(10004L, LocalDate.now());
        Assertions.assertTrue(skuPriceEntity.isPresent());
        Assertions.assertEquals(110,skuPriceEntity.get().getListPrice().doubleValue());
        Assertions.assertEquals(90,skuPriceEntity.get().getSalePrice().doubleValue());
    }

    @Test
    void findFuturePricesBySku() {

    }

    @Test
    void findPreviousPricesBySku() {
    }

    @Test
    void findFuturePriceBySku() {
        var skuPriceEntity =repositoryManager.getPriceRepository().findFuturePriceBySku(10004L, LocalDate.now());
        Assertions.assertTrue(skuPriceEntity.isPresent());
        Assertions.assertEquals(150,skuPriceEntity.get().getListPrice().doubleValue());
        Assertions.assertEquals(135,skuPriceEntity.get().getSalePrice().doubleValue());
    }
}