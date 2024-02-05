package com.app.services.impl;

import com.app.entites.Sku;
import com.app.payloads.SkuDTO;
import com.app.repositories.SkuRepo;
import com.app.services.SkuService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SkuServiceImpl implements SkuService {

    final ModelMapper modelMapper;
    final SkuRepo skuRepo;

    @Override
    public SkuDTO addSku(SkuDTO skuDTO) {
        Sku sku = modelMapper.map(skuDTO, Sku.class);
        skuRepo.save(sku);
        return modelMapper.map(sku, SkuDTO.class);
    }

    @Override
    public SkuDTO updateSku(Long skuId, SkuDTO skuDTO) {
        Sku sku = modelMapper.map(skuDTO, Sku.class);
        // sku.setSkuId(skuId);
        skuRepo.save(sku);
        return modelMapper.map(sku, SkuDTO.class);
    }

    @Override
    public String deleteSku(Long skuId) {
        skuRepo.deleteById(skuId);
        return "Sku " + skuId + " deleted successfully !!!";
    }
}
