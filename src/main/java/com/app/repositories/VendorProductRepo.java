package com.app.repositories;

import com.app.entites.VendorProduct;
import com.app.repositories.projections.ProductProjection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VendorProductRepo extends CrudRepository<VendorProduct, Long> {

  List<VendorProduct> findByVendorId(Long vendorId);

  @Query(
      "SELECT vp.productId FROM VendorProduct vp WHERE vp.vendorId = :vendorId AND vp.productId IN :productIds")
  Set<Long> validateAssignedProductIdsForVendor(
      @Param("vendorId") Long vendorId, @Param("productIds") Set<Long> productIds);

  @Query(
      "SELECT vp.id FROM VendorProduct vp WHERE vp.id=:vendorProductId and vp.vendorId = :vendorId")
  Optional<Long> findByIdAndVendorId(
      @Param("vendorId") Long vendorId, @Param("vendorProductId") Long vendorProductId);

  @Query(
      """
  SELECT vp.id as id, p.name as name
     FROM VendorProduct vp
     JOIN Product p ON p.id = vp.productId
     WHERE vp.vendorId = :vendorId
     ORDER BY p.name ASC
""")
  List<ProductProjection> findProductsByVendor(@Param("vendorId") Long vendorId);
}
