package com.app.repositories;

import com.app.entites.Sku;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuRepo extends JpaRepository<Sku, Long> {

  @Query("SELECT s FROM Sku s WHERE s.id = ?1 ")
  Optional<Sku> findByIdAndStoreId(final Long skuId);

  @Query("SELECT s FROM Sku s WHERE s.id = ?1 ")
  Optional<Sku> findByIdAndVendorId(final Long skuId, final Long vendorId);

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
                ts.product_id,
                tp.name AS product_name,
                ts.id,
                ts.image_path,
                ts.name AS sku_name,
                ts.size AS sku_size,
                 ts.stock,
                ts.type AS sku_type,
                ts.service_valid_days AS service_valid_days,
                led.price_id AS price_id,
                led.list_price AS latest_list_price,
                led.sale_price AS latest_sale_price,
                led.effective_date AS latest_effective_date,
                JSON_AGG(
                    JSON_BUILD_OBJECT(
                        'frequency', tsp.frequency,
                        'eligible_delivery_days', tssp.eligible_delivery_days
                    )
                ) AS eligible_subscription_options
            FROM
                tb_sku ts
            JOIN
                tb_product tp
                ON ts.product_id = tp.id
            JOIN
                tb_sku_sub_plan tssp
                ON tssp.sku_id = ts.id
            JOIN
                tb_sub_plan tsp
                ON tsp.id = tssp.sub_plan_id
            JOIN
                LatestEffectiveDates led
                ON ts.id = led.sku_id
            WHERE
                ts.vendor_id = :vendorId
                AND led.rn = 1
            GROUP BY
                ts.product_id, tp.name, ts.id, ts.image_path, ts.name, ts.size, ts.type, ts.service_valid_days,
                led.price_id, led.list_price, led.sale_price, led.effective_date;
            """,
      nativeQuery = true)
  List<Object[]> findVendorProductSkus(@Param("vendorId") Long vendorId);

  @Query("SELECT active from Sku WHERE id=:skuId")
  Boolean findSkuAvailable(Long skuId);

  @Query("SELECT s FROM Sku s WHERE s.vendorProductId = :vendorProductId AND s.id = :skuId")
  Optional<Sku> findByVendorProductAndSkuId(Long vendorProductId, Long skuId);
}
