package com.app.repositories;

import com.app.entites.VendorProduct;
import org.springframework.data.repository.CrudRepository;

public interface VendorProductRepo extends CrudRepository<VendorProduct, Long> {}
