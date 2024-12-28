package com.app.repositories;

import com.app.entites.VendorCategory;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface VendorCategoryRepo extends CrudRepository<VendorCategory, Long> {

  List<VendorCategory> findByVendorId(Long vendorId);
}
