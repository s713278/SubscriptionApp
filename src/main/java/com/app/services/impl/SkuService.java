package com.app.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.app.entites.Sku;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.SkuDTO;
import com.app.repositories.SkuRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SkuService {

    final ModelMapper modelMapper;
    final SkuRepo skuRepo;

    public SkuDTO addSku(SkuDTO skuDTO) {
        Sku sku = modelMapper.map(skuDTO, Sku.class);
        skuRepo.save(sku);
        return modelMapper.map(sku, SkuDTO.class);
    }

    public SkuDTO updateSku(Long skuId, SkuDTO skuDTO) {
        Sku sku = modelMapper.map(skuDTO, Sku.class);
        // sku.setSkuId(skuId);
        skuRepo.save(sku);
        return modelMapper.map(sku, SkuDTO.class);
    }

    @CacheEvict(key ="#skuId")
    public String deleteSku(Long skuId) {
        skuRepo.deleteById(skuId);
        return "Sku " + skuId + " deleted successfully !!!";
    }

    @Cacheable(value="skus",key ="#skuId")
    public SkuDTO fetchSkuById(final Long skuId){
        var sku = skuRepo.findById(skuId)
                .orElseThrow(()->new APIException(APIErrorCode.API_404,"SKU not existed in system."));
        return modelMapper.map(sku,SkuDTO.class);
    }

    public Sku fetchSkuEntityById(final Long skuId){
       return  skuRepo.findById(skuId)
                .orElseThrow(()->new APIException(APIErrorCode.API_404,"SKU not existed in system."));
    }
}
