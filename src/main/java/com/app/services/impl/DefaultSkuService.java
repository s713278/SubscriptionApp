package com.app.services.impl;

import com.app.entites.ServiceAttribute;
import com.app.entites.Sku;
import com.app.entites.SkuPrice;
import com.app.entites.SkuSubscriptionPlan;
import com.app.entites.type.SkuType;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.EligibleSubscriptionDTO;
import com.app.payloads.ProductSkuDTO;
import com.app.payloads.request.SkuCreateRequest;
import com.app.repositories.RepositoryManager;
import com.app.services.AbstractSkuService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@Slf4j
public class DefaultSkuService extends AbstractSkuService {

  public DefaultSkuService(
      UserService userService, RepositoryManager repoManager, ModelMapper modelMapper) {
    this.userService = userService;
    this.repositoryManager = repoManager;
    this.modelMapper = modelMapper;
  }

  public SkuCreateRequest updateSku(Long skuId, SkuCreateRequest skuDTO) {
    Sku sku = modelMapper.map(skuDTO, Sku.class);
    // sku.setSkuId(skuId);
    repositoryManager.getSkuRepo().save(sku);
    return modelMapper.map(sku, SkuCreateRequest.class);
  }

  @CacheEvict(key = "#skuId")
  public String deleteSku(Long skuId) {
    repositoryManager.getSkuRepo().deleteById(skuId);
    return "SKU " + skuId + " deleted successfully !!!";
  }

  @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
  public Sku fetchSkuEntityById(final Long skuId) {
    return repositoryManager
        .getSkuRepo()
        .findById(skuId)
        .orElseThrow(
            () -> new APIException(APIErrorCode.ENTITY_NOT_FOUND, "Invalid SKU ID " + skuId));
  }

  // @Cacheable(value = CacheType.CACHE_TYPE_VENDORS,key = "'vendor::product::' + #vendorId")
  @Transactional(readOnly = true)
  public Map<String, List<ProductSkuDTO>> fetchAllProductSkusByVendorId(Long vendorId) {
    var queryResults = repositoryManager.getSkuRepo().findVendorProductSkus(vendorId);
    List<ProductSkuDTO> productSkus =
        queryResults.stream()
            .map(
                result -> {
                  try {
                    return new ProductSkuDTO(
                        (Long) result[0], // productId
                        (String) result[1], // productName
                        (Long) result[2], // skuId
                        (String) result[3], // imagePath
                        (String) result[4], // skuName
                        (String) result[5], // skuSize
                        (Integer) result[6], // stock
                        (String) result[7], // type
                        (Integer) result[8], // valid_service_days,
                        (Long) result[9], // vendorSkuPriceId
                        ((BigDecimal) result[10]).doubleValue(), // listPrice
                        (result[11]) != null
                            ? ((BigDecimal) result[11]).doubleValue()
                            : 0, // salePrice
                        ((Date) result[12]).toLocalDate(), // effective_date,
                        // SubFrequency.valueOf((String)result[13]), //Frequency
                        //  new ObjectMapper().readValue((String)result[14], new
                        // TypeReference<Map<String, Object>>(){}) //Eligible_Options
                        new ObjectMapper()
                            .readValue(
                                (String) result[13],
                                new TypeReference<List<EligibleSubscriptionDTO>>() {}));
                  } catch (Exception e) {
                    log.error("Unable to parse SQL result into ProductSkuDTO  :", e);
                    throw new RuntimeException(e);
                  }
                })
            .toList();

    // Group the ProductSku objects by productId
    return productSkus.stream().collect(Collectors.groupingBy(ProductSkuDTO::productName));
  }

  @Override
  protected Sku processCreateSku(Long vendorProductId, SkuCreateRequest request) {
    var sku = modelMapper.map(request, Sku.class);
    sku.setVendorProductId(vendorProductId);
    sku.setSkuCode("SKU_" + vendorProductId + "_" + request.getName());
    // Service attributes
    if (request.getSkuType() == SkuType.SERVICE) {
      var serviceAttribute =
          modelMapper.map(request.getServiceAttributes(), ServiceAttribute.class);
      serviceAttribute.setSku(sku);
      sku.setServiceAttributes(serviceAttribute);
    } else if (request.getSkuType() == SkuType.ITEM) {
      List<SkuSubscriptionPlan> skuSubscriptionPlans = new ArrayList<>();
      request
          .getEligibleSubPlans()
          .forEach(
              subscriptionPlanDTO -> {
                getRepositoryManager()
                    .getSubscriptionPlanRepo()
                    .findById(subscriptionPlanDTO.getSubPlanId())
                    .ifPresent(
                        (subPlanEntity) -> {
                          skuSubscriptionPlans.add(
                              SkuSubscriptionPlan.builder()
                                  .sku(sku)
                                  .subscriptionPlan(subPlanEntity)
                                  .eligibleDeliveryDays(
                                      subscriptionPlanDTO.getEligibleDeliveryDays())
                                  .build());
                        });
              });
      log.debug("SKU Subscription Plans Size: {}", skuSubscriptionPlans.size());
      sku.setEligibleSubPlans(skuSubscriptionPlans);
    }
    var skuPriceList =
        request.getPriceList().stream()
            .map(
                skuPriceDTO -> {
                  SkuPrice skuPrice = modelMapper.map(skuPriceDTO, SkuPrice.class);
                  skuPrice.setSku(sku);
                  return skuPrice;
                })
            .collect(Collectors.toList());
    sku.setPriceList(skuPriceList);
    log.debug("SKU Price List Size : {}", skuPriceList.size());
    return sku;
  }

  public ServiceAttribute fetchServiceAttributesBySkuId(Long skuId) {
    return getRepositoryManager()
        .getServiceAttributesRepo()
        .findBySkuId(skuId)
        .orElseThrow(
            () ->
                new APIException(
                    APIErrorCode.ENTITY_NOT_FOUND,
                    "Service attributes not found for the SKU "
                        + skuId
                        + ". Please check its TYPE."));
  }
}
