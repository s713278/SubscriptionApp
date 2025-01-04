package com.app.repositories;

import com.app.entites.VendorCategory;
import com.app.repositories.projections.CategoryProjection;
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

  @Query(
      """
              SELECT c.id as id, c.name as name, c.imagePath as imagePath
                        FROM Category c
                        JOIN VendorCategory vc ON c.id = vc.categoryId
                        WHERE vc.vendorId = :vendorId
              """)
  List<CategoryProjection> findCategoriesByVendorId(Long vendorId);
}
