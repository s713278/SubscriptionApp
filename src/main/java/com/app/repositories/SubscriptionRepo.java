package com.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.entites.Subscription;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {

    @Query("Select id,quantity,frequency,status,startDate,nextDeliveryDate from Subscription s where s.id=?1")
    Optional<Subscription> findSubscription(final Long subId);

    @Query(value = """
        SELECT 
            sub.id,
            sub.customer_id,
            sku.vendor_id,
            vendor.business_name,
            sku.list_price,
            sku.sale_price,
            sub.quantity,
            sku.list_price * sub.quantity,
            sku.size,
            sub.start_date,
            sub.frequency,
            sub.status,
            sub.next_delivery_date
        FROM 
            tb_subscription sub
        JOIN
            tb_sku sku ON sub.sku_id= sku.id
        JOIN 
            tb_vendor vendor ON vendor.id = :vendorId
        WHERE 
            sub.customer_id = :userId
        """, nativeQuery = true)
    List<Object[]> findByUserIdAndVendorId(@Param("userId") Long userId, @Param("vendorId")  Long vendorId);
    
   // List<Subscription> findByVendorIdAndStatus(final Long vendorId,SubscriptionStatus status);


    @Query(value = """
        SELECT 
            sub.id,
            sub.customer_id,
            sku.vendor_id,
            vendor.business_name,
            sku.list_price,
            sku.sale_price,
            sub.quantity,
            sku.list_price * sub.quantity,
            sku.size,
            sub.start_date,
            sub.frequency,
            sub.status,
            sub.next_delivery_date
        FROM 
            tb_subscription sub
        JOIN
            tb_sku sku ON sub.sku_id=sku.id
        JOIN 
            tb_vendor vendor ON vendor.id = :vendorId
        """, nativeQuery = true)
    List<Object[]> findByVendorId(@Param("vendorId") Long vendorId);

    Optional<Subscription> findByUserIdAndSkuId(final Long userId, final Long vendorId);


    @Query(value = """
        SELECT 
            sub.id,
            sub.customer_id,
            sku.vendor_id,
            vendor.business_name,
            sku.list_price,
            sku.sale_price,
            sub.quantity,
            sku.list_price * sub.quantity,
            sku.size,
            sub.start_date,
            sub.frequency,
            sub.status,
            sub.next_delivery_date
        FROM 
            tb_subscription sub
        JOIN
            tb_sku sku ON sub.sku_id=sku.id
        JOIN 
            tb_vendor vendor ON vendor.id = sku.vendor_id
        WHERE 
            sub.customer_id = :userId
        """, nativeQuery = true)
    List<Object[]> findByUserId(@Param("userId") Long userId);

    Optional<Subscription> findByIdAndUserId(Long subId,Long userId);
}
