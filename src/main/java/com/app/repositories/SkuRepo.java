package com.app.repositories;

import com.app.entites.Sku;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuRepo extends JpaRepository<Sku, Long> {

  @Query("SELECT s FROM Sku s WHERE s.id = ?1 ")
  Optional<Sku> findByIdAndStoreId(final Long skuId);

  @Query(
      value =
          """
            WITH LatestEffectiveDates AS (
                SELECT
                    id AS price_id,
                    sku_id,
                    list_price,
                    sale_price,
                    effective_date,
                    ROW_NUMBER() OVER (PARTITION BY sku_id ORDER BY effective_date DESC) AS rn
                FROM
                    tb_sku_price
                WHERE
                    effective_date <= CURRENT_DATE
            )
            SELECT
                ts.vendor_product_id AS vendor_product_id,
                tp.name AS product_name,
                ts.id,
                ts.image_path,
                ts.name AS sku_name,
                ts.weight AS sku_size,
                ts.stock,
                ts.type AS sku_type,
                tsa.valid_days AS valid_days,
                led.price_id AS price_id,
                led.list_price AS latest_list_price,
                led.sale_price AS latest_sale_price,
                led.effective_date AS latest_effective_date,
                JSON_AGG(
                    JSON_BUILD_OBJECT(
                        'frequency', tsp.frequency,
                        'eligible_delivery_days', tssp.eligible_delivery_days
                    )
                ) AS eligible_subscription_details
            FROM
                tb_sku ts
            JOIN
                tb_product_vendor tpv
                ON ts.vendor_product_id = tpv.id
            JOIN
                tb_product tp
                ON tpv.product_id = tp.id
            LEFT JOIN
                tb_service_attributes tsa
                ON ts.id = tsa.sku_id AND ts.type = 'SERVICE'
            LEFT JOIN
                tb_sku_sub_plan tssp
                ON ts.id = tssp.sku_id
            LEFT JOIN
                tb_sub_plan tsp
                ON tsp.id = tssp.sub_plan_id
            JOIN
                LatestEffectiveDates led
                ON ts.id = led.sku_id AND led.rn = 1
            WHERE
                tpv.vendor_id = :vendorId -- Filter by vendor_id
            GROUP BY
                ts.vendor_product_id, product_name, ts.id, ts.image_path, sku_name, sku_size, sku_type, valid_days,
                led.price_id, led.list_price, led.sale_price, led.effective_date;
            """,
      nativeQuery = true)
  List<Object[]> findVendorProductSkus(@Param("vendorId") Long vendorId);

  @Query("SELECT active from Sku WHERE id=:skuId")
  Boolean findSkuAvailable(Long skuId);

  @Query("SELECT s FROM Sku s WHERE s.vendorProductId = :vendorProductId AND s.id = :skuId")
  Optional<Sku> findByVendorProductAndSkuId(Long vendorProductId, Long skuId);

  @Query(
      value =
          """
          WITH LatestEffectiveDates AS (
             SELECT
                 id AS price_id,
                 sku_id,
                 list_price,
                 sale_price,
                 effective_date,
                 ROW_NUMBER() OVER (PARTITION BY sku_id ORDER BY effective_date DESC) AS rn
             FROM
                 tb_sku_price
             WHERE
                 effective_date <= CURRENT_DATE
         )
         SELECT
             ts.vendor_product_id AS vendorProductId,
             ts.id AS skuId,
             ts.name AS skuName,
             ts.image_path AS imagePath,
             ts.weight AS skuSize,
             ts.type AS skuType,
             ts.is_active AS active,
             tsa.valid_days AS validDays,
             led.price_id AS priceId,
             led.list_price AS latestListPrice,
             led.sale_price AS latestSalePrice,
             led.effective_date AS latestEffectiveDate,
             JSON_AGG(
                 JSON_BUILD_OBJECT(
                     'id', tssp.id,
                     'frequency', tsp.frequency,
                     'eligibleDeliveryDays', COALESCE(tssp.eligible_delivery_days->'delivery_days', '[]')
                 )
             ) AS eligibleSubscriptionDetails,
             ts.subscription_eligible AS subscriptionEligible
         FROM
             tb_sku ts
         LEFT JOIN
             tb_service_attributes tsa
             ON ts.id = tsa.sku_id AND ts.type = 'SERVICE'
         LEFT JOIN
             tb_sku_sub_plan tssp
             ON ts.id = tssp.sku_id
         LEFT JOIN
             tb_sub_plan tsp
             ON tsp.id = tssp.sub_plan_id
         JOIN
             LatestEffectiveDates led
             ON ts.id = led.sku_id AND led.rn = 1
         WHERE
             ts.vendor_product_id = :vendorProductId
         GROUP BY
             ts.vendor_product_id, ts.id, ts.image_path, ts.name, ts.weight, ts.type, ts.is_active, tsa.valid_days,
             led.price_id, led.list_price, led.sale_price, led.effective_date,ts.subscription_eligible;
        """,
      countQuery =
          """
            SELECT COUNT(*)
            FROM tb_sku ts
            WHERE ts.vendor_product_id = :vendorProductId
        """,
      nativeQuery = true)
  Page<Object[]> findSkusByVendorProductId(
      @Param("vendorProductId") Long vendorProductId, Pageable pageDetails);

  @Query(
      value =
          """
         WITH LatestEffectiveDates AS (
             SELECT
                 id AS price_id,
                 sku_id,
                 list_price,
                 sale_price,
                 effective_date,
                 ROW_NUMBER() OVER (PARTITION BY sku_id ORDER BY effective_date DESC) AS rn
             FROM
                 tb_sku_price
             WHERE
                 effective_date <= CURRENT_DATE
         )
         SELECT
             ts.vendor_product_id AS vendorProductId,
             ts.id AS skuId,
             ts.name AS skuName,
             ts.image_path AS imagePath,
             ts.weight AS skuSize,
             ts.type AS skuType,
             ts.is_active AS active,
             tsa.valid_days AS validDays,
             led.price_id AS priceId,
             led.list_price AS latestListPrice,
             led.sale_price AS latestSalePrice,
             led.effective_date AS latestEffectiveDate,
             JSON_AGG(
                 JSON_BUILD_OBJECT(
                     'id', tssp.id,
                     'frequency', tsp.frequency,
                     'eligibleDeliveryDays', COALESCE(tssp.eligible_delivery_days->'delivery_days', '[]')
                 )
             ) AS eligibleSubscriptionDetails,
             ts.subscription_eligible AS subscriptionEligible
         FROM
             tb_sku ts
         JOIN
             tb_product_vendor tpv
             on ts.vendor_product_id  = tpv.id
         LEFT JOIN
             tb_service_attributes tsa
             ON ts.id = tsa.sku_id AND ts.type = 'SERVICE'
         LEFT JOIN
             tb_sku_sub_plan tssp
             ON ts.id = tssp.sku_id
         LEFT JOIN
             tb_sub_plan tsp
             ON tsp.id = tssp.sub_plan_id
         JOIN
             LatestEffectiveDates led
             ON ts.id = led.sku_id AND led.rn = 1
         WHERE
             tpv.vendor_id = :vendorId
         GROUP BY
             ts.vendor_product_id, ts.id, ts.image_path, ts.name, ts.weight, ts.type, ts.is_active, tsa.valid_days,
             led.price_id, led.list_price, led.sale_price, led.effective_date,ts.subscription_eligible
         """,
      countQuery =
          """
         WITH LatestEffectiveDates AS (
             SELECT
                 sku_id,
                 ROW_NUMBER() OVER (PARTITION BY sku_id ORDER BY effective_date DESC) AS rn
             FROM
                 tb_sku_price
             WHERE
                 effective_date <= CURRENT_DATE
         )
         SELECT COUNT(*)
         FROM tb_sku ts
         JOIN tb_product_vendor tpv ON ts.vendor_product_id = tpv.id
         JOIN LatestEffectiveDates led ON ts.id = led.sku_id AND led.rn = 1
         WHERE tpv.vendor_id = :vendorId
         """,
      nativeQuery = true)
  Page<Object[]> findSkusByVendorId(@Param("vendorId") Long vendorId, Pageable pageable);
}
