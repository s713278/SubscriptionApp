package com.app.repositories;

import com.app.entites.VendorSkuPrice;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface VendorSkuPriceRepo  extends CrudRepository<VendorSkuPrice,Long>{


    public Optional<VendorSkuPrice> findByVendorIdAndSkuId(Long vendorId, Long skuId);

}
