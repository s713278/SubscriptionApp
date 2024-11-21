package com.app.services.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entites.Sku;
import com.app.entites.type.SubFrequency;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.ProductSkuDTO;
import com.app.payloads.SkuDTO;
import com.app.repositories.RepositoryManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SkuService {

    private static final Logger log = LoggerFactory.getLogger(SkuService.class);
    final RepositoryManager repositoryManager;
    final ModelMapper modelMapper;

    public SkuDTO addSku(SkuDTO skuDTO) {
        Sku sku = modelMapper.map(skuDTO, Sku.class);
        repositoryManager.getSkuRepo().save(sku);
        return modelMapper.map(sku, SkuDTO.class);
    }

    public SkuDTO updateSku(Long skuId, SkuDTO skuDTO) {
        Sku sku = modelMapper.map(skuDTO, Sku.class);
        // sku.setSkuId(skuId);
        repositoryManager.getSkuRepo().save(sku);
        return modelMapper.map(sku, SkuDTO.class);
    }

    @CacheEvict(key = "#skuId")
    public String deleteSku(Long skuId) {
        repositoryManager.getSkuRepo().deleteById(skuId);
        return "Sku " + skuId + " deleted successfully !!!";
    }

    @Cacheable(value = "skus", key = "#skuId")
    public SkuDTO fetchSkuById(final Long skuId) {
        var sku = repositoryManager.getSkuRepo().findById(skuId)
                .orElseThrow(() -> new APIException(APIErrorCode.API_404, "SKU not existed in system."));
        return modelMapper.map(sku, SkuDTO.class);
    }

    public Sku fetchSkuEntityById(final Long skuId) {
        return repositoryManager.getSkuRepo().findById(skuId)
                .orElseThrow(() -> new APIException(APIErrorCode.API_404, "SKU not existed in system."));
    }



   // @Cacheable(value = CacheType.CACHE_TYPE_VENDORS,key = "'vendor::product::' + #vendorId")
    @Transactional(readOnly = true)
    public Map<String, List<ProductSkuDTO>> fetchProductSkusByVendorId(Long vendorId){
        var queryResults = repositoryManager.getSkuRepo().findVendorProductSkus(vendorId);
        List<ProductSkuDTO> productSkus = queryResults.stream().map(result ->
                {
                    try {
                        return new ProductSkuDTO(
                                (Long) result[0],         // productId
                                (String) result[1],       // productName
                                (Long) result[2],         // skuId
                                (String) result[3],       // imagePath
                                (String) result[4],       // skuName
                                (String) result[5],       // skuSize
                                (Integer) result[6],      // stock
                                (String)result[7],          //type
                                (Integer)result[8],         //valid_service_days,
                                (Long) result[9],         // vendorSkuPriceId
                                 ((BigDecimal) result[10]).doubleValue(),       // listPrice
                                (result[11])!=null?((BigDecimal) result[11]).doubleValue():0,     // salePrice
                                ((Date)result[12]).toLocalDate() ,//effective_date,
                                SubFrequency.valueOf((String)result[13]),
                              new ObjectMapper().readValue((String)result[14], new TypeReference<Map<String, Object>>(){})
                        );
                    } catch (JsonProcessingException e) {
                        log.error("Unable to parse json data result[14]",e);
                        throw new RuntimeException(e);
                    }
                }
        ).toList();

        // Group the ProductSku objects by productId
        return productSkus.stream()
                .collect(Collectors
                        .groupingBy(ProductSkuDTO::productName));
    }



}
