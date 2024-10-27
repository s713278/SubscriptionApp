package com.app.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.constants.CacheType;
import com.app.entites.VendorSkuPrice;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.ProductSkuDTO;
import com.app.repositories.RepositoryManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class VendorSkuPriceService {

    private final RepositoryManager repositoryManager;

    public BigDecimal getPriceForSku(Long vendorId, Long skuId) {
        return repositoryManager.getVendorSkuPriceRepo().findByVendorIdAndSkuId(vendorId, skuId)
                                       .map(VendorSkuPrice::getSalePrice)
                                       .orElseThrow(() -> new RuntimeException("Price not found!"));
    }


    @Cacheable(value = CacheType.CACHE_TYPE_VENDORS,key = "'vendor::product::' + #vendorId")
    @Transactional(readOnly = true)
    public Map<Long, List<ProductSkuDTO>> fetchProductsByVendorId(Long vendorId){
        repositoryManager.getVendorRepo()
                .findById(vendorId).orElseThrow(()->new APIException(APIErrorCode.API_400,"Invalid vendor id"));
        var queryResults = repositoryManager.getVendorSkuPriceRepo().findVendorProducts(vendorId);

        List<ProductSkuDTO> productSkus = queryResults.stream().map(result ->
                new ProductSkuDTO(
                        (Long) result[0],         // productId
                        (String) result[1],       // productName
                        (Long) result[2],         // skuId
                        (String) result[3],       // imagePath
                        (String) result[4],       // skuName
                        (String) result[5],       // skuSize
                        (Long) result[6],         // vendorSkuPriceId
                        (Integer) result[7],      // stock
                        ((BigDecimal) result[8]).doubleValue(),       // listPrice
                        ((BigDecimal) result[9]).doubleValue()     // salePrice
                )
        ).toList();

        // Group the ProductSku objects by productId
        return productSkus.stream()
                .collect(Collectors
                        .groupingBy(ProductSkuDTO::productId));
    }

}
