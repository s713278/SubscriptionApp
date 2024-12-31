package com.app.services;

import com.app.entites.Sku;
import com.app.entites.SkuPrice;
import com.app.entites.type.*;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.SkuCreateRequest;
import com.app.payloads.response.CreateItemResponse;
import com.app.payloads.response.SkuResponse;
import com.app.repositories.RepositoryManager;
import com.app.services.impl.UserService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

@Setter
@Getter
@Slf4j
public abstract class AbstractSkuService implements SkuService {

  protected ModelMapper modelMapper;
  protected RepositoryManager repositoryManager;
  protected UserService userService;

  @Transactional
  @Override
  public final CreateItemResponse createSku(
      Long vendorId, Long productId, SkuCreateRequest request) {
    preValidation(vendorId, productId, request);
    Sku sku = processCreateSku(productId, request);
    sku = getRepositoryManager().getSkuRepo().save(sku);
    postUpdate(sku);
    return new CreateItemResponse(sku.getId(), "SKU created successfully.");
  }

  protected abstract Sku processCreateSku(Long vendorProductId, SkuCreateRequest skuCreateRequest);

  /**
   * All the business validation takes place before creating the SKI in database.
   *
   * @param vendorId
   * @param vendorProductId
   * @param request
   */
  protected void preValidation(Long vendorId, Long vendorProductId, SkuCreateRequest request) {
    // Step#1:Vendor active or not
    repositoryManager
        .getVendorRepo()
        .findByStatus(vendorId, ApprovalStatus.APPROVED, VendorStatus.ACTIVE)
        .orElseThrow(
            () ->
                new APIException(
                    APIErrorCode.SKU_CREATION_VALIDATION,
                    "Vendor with ID " + vendorId + " is not yet approved by admin."));
    // Step#2: Validate VendorProduct existence
    repositoryManager
        .getVendorProductRepo()
        .findByIdAndVendorId(vendorId, vendorProductId)
        .orElseThrow(
            () ->
                new APIException(
                    APIErrorCode.BAD_REQUEST_RECEIVED,
                    "Vendor Product with ID " + vendorProductId + " not found"));
    // Step#3: Validate prices
    if (request.getPriceList() == null || request.getPriceList().isEmpty()) {
      throw new IllegalArgumentException("SKU must have at least one price");
    }
  }

  protected void postUpdate(Sku sku) {}

  @Override
  public void updateSkuStatus(Long vendorId, Long productId, Long skuId, boolean activeStatus) {
    postUpdate(null);
  }

  @Override
  public SkuResponse fetchSkuById(Long skuId) {
    var skuEntity =
        repositoryManager
            .getSkuRepo()
            .findById(skuId)
            .orElseThrow(
                () ->
                    new APIException(
                        APIErrorCode.ENTITY_NOT_FOUND,
                        "VendorProduct with ID " + skuId + " not found"));
    return modelMapper.map(skuEntity, SkuResponse.class);
  }

  @Override
  public void addSkuPrice(Long skuId, SkuPrice skuPrice) {
    preValidation(skuPrice);
  }

  protected void preValidation(SkuPrice skuPrice) {}
}
