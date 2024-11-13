package com.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entites.Sku;

@Repository
public interface SkuRepo extends JpaRepository<Sku, Long> {

    @Query("SELECT s FROM Sku s WHERE s.id = ?1 ")
    Optional<Sku> findByIdAndStoreId(final Long skuId);
   
    @Query("SELECT s FROM Sku s WHERE s.id = ?1 ")
    Optional<Sku>  findByIdAndVendorId(final Long skuId,final Long vendorId);

    @Query(value = " SELECT  ts.product_id,tp.name as product_name, ts.id as sku_id,ts.image_path,ts.name as sku_name,ts.size as sku_size,"+
            "ts.id as sku_id, ts.stock ,ts.list_price,ts.sale_price,ts.effective_date "+
            "FROM tb_sku ts "+
            "JOIN tb_product tp ON ts.product_id = tp.product_id "+
            "WHERE ts.vendor_id = :vendorId",nativeQuery = true)
    List<Object[]> findVendorProductSkus(@Param("vendorId") Long vendorId);

}
