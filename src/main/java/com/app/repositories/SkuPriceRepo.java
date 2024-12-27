package com.app.repositories;

import com.app.entites.SkuPrice;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SkuPriceRepo extends JpaRepository<SkuPrice, Long> {

  // Fetch today's price based on the max effective_date <= today's date
  @Query(
      """
           SELECT sp
           FROM SkuPrice sp
           WHERE sp.sku.id = :skuId
             AND sp.effectiveDate <= :today
           ORDER BY sp.effectiveDate DESC
           LIMIT 1
           """)
  Optional<SkuPrice> findTodayPriceBySku(
      @Param("skuId") Long skuId, @Param("today") LocalDate today);

  // Fetch future prices for a given SKU
  @Query(
      "SELECT sp FROM SkuPrice sp WHERE sp.sku.id = :skuId AND sp.effectiveDate > :today ORDER BY sp.effectiveDate ASC")
  List<SkuPrice> findFuturePricesBySku(@Param("skuId") Long skuId, @Param("today") LocalDate today);

  // Fetch future prices for a given SKU
  @Query(
      "SELECT sp FROM SkuPrice sp WHERE sp.sku.id = :skuId AND sp.effectiveDate <= :today ORDER BY sp.effectiveDate DESC")
  List<SkuPrice> findPreviousPricesBySku(
      @Param("skuId") Long skuId, @Param("today") LocalDate today);

  // Fetch today's price based on the max effective_date <= today's date
  @Query(
      """
       SELECT sp
       FROM SkuPrice sp
       WHERE sp.sku.id = :skuId
         AND sp.effectiveDate > :today
       ORDER BY sp.effectiveDate ASC
       LIMIT 1
       """)
  Optional<SkuPrice> findFuturePriceBySku(
      @Param("skuId") Long skuId, @Param("today") LocalDate today);
}
