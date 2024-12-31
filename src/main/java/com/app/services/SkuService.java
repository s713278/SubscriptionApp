package com.app.services;

import com.app.entites.SkuPrice;
import com.app.payloads.request.SkuCreateRequest;
import com.app.payloads.response.CreateItemResponse;
import com.app.payloads.response.SkuResponse;

public interface SkuService {

  CreateItemResponse createSku(Long vendorId, Long productId, SkuCreateRequest request);

  void updateSkuStatus(Long vendorId, Long productId, Long skuId, boolean activeStatus);

  SkuResponse fetchSkuById(Long skuId);

  void addSkuPrice(Long skuId, SkuPrice skuPrice);
}
