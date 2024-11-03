package com.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entites.VendorSkuPrice;

@Repository
public interface VendorSkuPriceRepo  extends CrudRepository<VendorSkuPrice,Long>{


    public Optional<VendorSkuPrice> findByVendorIdAndSkuId(Long vendorId, Long skuId);

    /*@Query("SELECT new com.app.payloads.ProductSkuDTO(ts.product.productId, ts.product.name, vsp) " +
            "FROM VendorSkuPrice vsp " +
            "JOIN vsp.sku ts " +
            "WHERE vsp.vendor.id = :vendorId")
    List<ProductSkuDTO> findVendorProducts(@Param("vendorId") Long vendorId);


    /*@Query("SELECT ts.product.productId, ts.product.name, vsp " +
            "FROM VendorSkuPrice vsp " +
            "JOIN vsp.sku ts " +
            "WHERE vsp.vendor.id = :vendorId")*/

    @Query(value = " SELECT  ts.product_id,tp.name as product_name, ts.id as sku_id,ts.image_path,ts.name as sku_name,ts.size as sku_size,"+
            "tvsp.id as vendor_sku_proce_id, tvsp.stock ,tvsp.list_price,tvsp.sale_price "+
    "FROM tb_vendor_sku_price tvsp "+
    "JOIN tb_sku ts ON tvsp.sku_id = ts.id "+
    "JOIN tb_product tp ON ts.product_id = tp.product_id "+
    "WHERE tvsp.vendor_id = :vendorId",nativeQuery = true)
    List<Object[]> findVendorProducts(@Param("vendorId") Long vendorId);

}
