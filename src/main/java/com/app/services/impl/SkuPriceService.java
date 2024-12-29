package com.app.services.impl;

import com.app.constants.CacheType;
import com.app.entites.SkuPrice;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.repositories.RepositoryManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkuPriceService {

  private final RepositoryManager repositoryManager;

  /**
   * Get today's price for a specific SKU.
   *
   * @param skuId The SKU ID.
   * @return The price details for today or null if not found.
   */
  @Cacheable(value = CacheType.CACHE_TYPE_PRICES, key = "#skuId")
  public SkuPrice fetchTodayPriceBySkuId(Long skuId) {
    LocalDate today = LocalDate.now();
    log.debug("Fetching today's price for sku : {}", skuId);
    Optional<SkuPrice> todayPrice =
        repositoryManager.getPriceRepository().findTodayPriceBySku(skuId, today);
    return todayPrice.orElseThrow(
        () ->
            new APIException(
                APIErrorCode.BAD_REQUEST_RECEIVED, "No price found in pricing table for " + skuId));
  }

  /**
   * Get all previous prices for a specific SKU.
   *
   * @param skuId The SKU ID.
   * @return List of future prices ordered by effective date.
   */
  @Cacheable(value = CacheType.CACHE_TYPE_PRICES, key = "'previous_'#skuId")
  public List<SkuPrice> fetchPreviousPriceBySkuId(Long skuId) {
    LocalDate today = LocalDate.now();
    return repositoryManager.getPriceRepository().findPreviousPricesBySku(skuId, today);
  }

  /**
   * Get all future prices for a specific SKU.
   *
   * @param skuId The SKU ID.
   * @return List of future prices ordered by effective date.
   */
  @Cacheable(value = CacheType.CACHE_TYPE_PRICES, key = "'future_'#skuId")
  public List<SkuPrice> fetchAllFuturePricesBySkuId(Long skuId) {
    LocalDate today = LocalDate.now();
    return repositoryManager.getPriceRepository().findFuturePricesBySku(skuId, today);
  }

  /**
   * Get today's price for a specific SKU.
   *
   * @param skuId The SKU ID.
   * @return The price details for today or null if not found.
   */
  @Cacheable(value = CacheType.CACHE_TYPE_PRICES, key = "'today_'#skuId")
  public SkuPrice fetchFuturePriceBySkuId(Long skuId) {
    LocalDate today = LocalDate.now();
    log.debug("Fetching next increased price for sku : {}", skuId);
    Optional<SkuPrice> todayPrice =
        repositoryManager.getPriceRepository().findFuturePriceBySku(skuId, today);
    return todayPrice.orElse(null);
  }
}
