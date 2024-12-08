package com.app.repositories;

import com.app.entites.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {

    @Query("Select id,quantity,frequency,status,startDate,nextDeliveryDate from Subscription s where s.id=?1")
    Optional<Subscription> findSubscription(final Long subId);
    @Query(value = """
        WITH LatestEffectiveDates
          AS ( SELECT
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
              sub.id as sub_id,
              sub.customer_id,
              sku.vendor_id,
              vendor.business_name,
              led.list_price,
              led.sale_price,
              sub.quantity,
              led.sale_price * sub.quantity,
              sku.size,
              sub.start_date,
              sub.frequency,
              sub.status,
              sub.next_delivery_date,
              sku."name" ,
              sku.image_path
          FROM
              tb_subscription sub
          JOIN
              tb_sku sku
              ON sub.sku_id=sku.id
          JOIN
              LatestEffectiveDates led
              ON
              led.rn = 1 and sku.id = led.sku_id
          JOIN
              tb_vendor vendor
              ON vendor.id = :vendorId and vendor.id = sku.vendor_id
          WHERE
              sub.customer_id = :userId
        """, nativeQuery = true)
    List<Object[]> findByUserIdAndVendorId(@Param("userId") Long userId, @Param("vendorId")  Long vendorId);
    
   // List<Subscription> findByVendorIdAndStatus(final Long vendorId,SubscriptionStatus status);
    @Query(value = """
        WITH LatestEffectiveDates
          AS ( SELECT
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
              sub.id as sub_id,
              sub.customer_id,
              sku.vendor_id,
              vendor.business_name,
              led.list_price,
              led.sale_price,
              sub.quantity,
              led.sale_price * sub.quantity,
              sku.size,
              sub.start_date,
              sub.frequency,
              sub.status,
              sub.next_delivery_date,
              sku."name" ,
              sku.image_path
          FROM
              tb_subscription sub
          JOIN
              tb_sku sku
              ON sub.sku_id=sku.id
          JOIN
              LatestEffectiveDates led
              ON
              led.rn = 1 and sku.id = led.sku_id
          JOIN
              tb_vendor vendor
              ON vendor.id = :vendorId and vendor.id = sku.vendor_id
        """, nativeQuery = true)
    List<Object[]> findByVendorId(@Param("vendorId") Long vendorId);

    Optional<Subscription> findByUserIdAndSkuId(final Long userId, final Long vendorId);


    @Query(value = """
            WITH LatestEffectiveDates
            AS ( SELECT
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
                sub.id,
                sub.customer_id,
                sku.vendor_id,
                vendor.business_name,
                led.list_price,
                led.sale_price,
                sub.quantity,
                led.sale_price * sub.quantity,
                sku.size,
                sub.start_date,
                sub.frequency,
                sub.status,
                sub.next_delivery_date,
                sku."name" ,
                sku.image_path
            FROM
                tb_subscription sub
            JOIN
                tb_sku sku
                ON sub.sku_id=sku.id
            JOIN
                LatestEffectiveDates led
                ON
                led.rn = 1 and sku.id = led.sku_id
            JOIN
                tb_vendor vendor
                ON vendor.id = sku.vendor_id
            WHERE
                sub.customer_id = :userId
        """, nativeQuery = true)
    List<Object[]> findByUserId(@Param("userId") Long userId);

    Optional<Subscription> findByIdAndUserId(Long subId,Long userId);
}
