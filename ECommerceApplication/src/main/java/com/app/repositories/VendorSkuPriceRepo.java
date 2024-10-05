package com.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.app.entites.VendorSkuPrice;

public interface VendorSkuPriceRepo  extends CrudRepository<VendorSkuPrice,Long>{


	public Optional<VendorSkuPrice> findByVendorIdAndSkuId(Long vendorId, Long skuId);

}
