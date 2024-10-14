package com.app.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.app.entites.VendorSkuPrice;
import com.app.repositories.VendorSkuPriceRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class VendorSkuPriceService {
    
    private final VendorSkuPriceRepo vendorSkuPriceRepo;

    public BigDecimal getPriceForSku(Long vendorId, Long skuId) {
        return vendorSkuPriceRepo.findByVendorIdAndSkuId(vendorId, skuId)
                                       .map(VendorSkuPrice::getSalePrice)
                                       .orElseThrow(() -> new RuntimeException("Price not found!"));
    }

}
