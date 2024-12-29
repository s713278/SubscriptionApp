package com.app.repositories;

import com.app.entites.VendorCategory;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VendorCategoryRepo extends CrudRepository<VendorCategory, Long> {

  List<VendorCategory> findByVendorId(Long vendorId);

  @Query(
      "SELECT vc.categoryId FROM VendorCategory vc WHERE vc.vendorId = :vendorId AND vc.categoryId IN :categoryIds")
  Set<Long> findAssignedCategoryIDsForVendor(
      @Param("vendorId") Long vendorId, @Param("categoryIds") Set<Long> categoryIds);
}
