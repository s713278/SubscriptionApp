package com.app.services;

import com.app.payloads.SkuDTO;

public interface SkuService {

	SkuDTO addProduct(SkuDTO skuDTO);

	SkuDTO updateProduct(Long skuId, SkuDTO skuDTO);

	String deleteSku(Long skuId);

}
