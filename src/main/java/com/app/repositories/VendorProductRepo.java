package com.app.repositories;

import com.app.entites.VendorProduct;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VendorProductRepo extends CrudRepository<VendorProduct, Long> {

  List<VendorProduct> findByVendorId(Long vendorId);

  @Query(
      "SELECT vp.productId FROM VendorProduct vp WHERE vp.vendorId = :vendorId AND vp.productId IN :productIds")
  Set<Long> findAssignedProductIdsForVendor(
      @Param("vendorId") Long vendorId, @Param("productIds") Set<Long> productIds);
}
