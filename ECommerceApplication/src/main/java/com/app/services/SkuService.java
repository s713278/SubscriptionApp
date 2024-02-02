package com.app.services;

import com.app.payloads.SkuDTO;

public interface SkuService {

	SkuDTO addSku(SkuDTO skuDTO);

	SkuDTO updateSku(Long skuId, SkuDTO skuDTO);

	String deleteSku(Long skuId);

}
