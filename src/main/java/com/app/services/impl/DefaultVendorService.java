package com.app.services.impl;

import com.app.constants.CacheType;
import com.app.constants.NotificationType;
import com.app.entites.Vendor;
import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.SkuType;
import com.app.entites.type.UserRoleEnum;
import com.app.entites.type.VendorStatus;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.*;
import com.app.payloads.request.VendorProfileRequest;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.PaginationResponse;
import com.app.repositories.RepositoryManager;
import com.app.repositories.projections.CategoryProjection;
import com.app.repositories.projections.ProductProjection;
import com.app.services.auth.dto.UserAuthentication;
import com.app.services.notification.NotificationContext;
import com.app.services.notification.NotificationTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Getter
public class DefaultVendorService extends AbstractVendorService {

  public DefaultVendorService(
      NotificationContext notificationContext,
      UserService userService,
      RepositoryManager repoManager,
      ModelMapper modelMapper) {
    this.notificationContext = notificationContext;
    this.userService = userService;
    this.repoManager = repoManager;
    this.modelMapper = modelMapper;
  }

  @Async
  protected void postCreateVendorProfile(Long vendorId, String mobileNumber) {
    log.info("Inviting vendor :{} to complete to complete onboarding process.", vendorId);
    getNotificationContext()
        .notifyUser(NotificationType.SMS, NotificationTemplate.PROFILE_UPDATES, mobileNumber);
  }

  @Cacheable(value = CacheType.CACHE_TYPE_VENDORS, key = "#status")
  public List<VendorBasicDTO> fetchVendorsByStatus(VendorStatus status) {
    var vendors = repoManager.getVendorRepo().findAllByStatus(status);
    return vendors.stream().map(vendor -> modelMapper.map(vendor, VendorBasicDTO.class)).toList();
  }

  @Transactional(readOnly = false)
  public void updateApprovalStatus(Long vendorId, ApprovalStatus approvalStatus) {
    log.debug("DB update request for vendor's approval status of user_id : {} ", vendorId);
    var isExisted = repoManager.getVendorRepo().existsById(vendorId);
    if (!isExisted) {
      throw new APIException(
          APIErrorCode.ENTITY_NOT_FOUND,
          "Vendor details not defined in system for user " + vendorId);
    }
    VendorStatus vendorStatus = VendorStatus.INACTIVE;
    if (Objects.requireNonNull(approvalStatus) == ApprovalStatus.APPROVED) {
      vendorStatus = VendorStatus.ACTIVE;
    }
    repoManager.getVendorRepo().updateApprovalStatus(vendorId, approvalStatus, vendorStatus);
  }

  @Transactional(readOnly = false)
  public void updateVendorStatus(Long vendorId, VendorStatus vendorStatus) {
    log.debug("DB update request for vendor's status of user_id : {} ", vendorId);
    var isExisted = repoManager.getVendorRepo().existsById(vendorId);
    if (!isExisted) {
      throw new APIException(
          APIErrorCode.ENTITY_NOT_FOUND,
          "Vendor details not defined in system for user " + vendorId);
    }
    repoManager.getVendorRepo().updateVendorStatus(vendorId, vendorStatus);
  }

  @PostConstruct
  public void postConstruct() {
    log.debug("{}", this.modelMapper);
    log.debug("{}", this.notificationContext);
    log.debug("{}", this.userService);
    log.debug("{}", this.repoManager);
  }

  @Transactional
  public APIResponse<String> deleteVendor(Long vendorId) {
    Vendor store =
        repoManager
            .getVendorRepo()
            .findById(vendorId)
            .orElseThrow(() -> new ResourceNotFoundException("Store", "storeId", vendorId));
    repoManager.getVendorRepo().deleteById(vendorId);
    return APIResponse.success(
        HttpStatus.OK.value(), "Store with id: " + vendorId + " deleted successfully !!!");
  }

  // @Cacheable(value =CacheType.CACHE_TYPE_VENDORS,key = "#serviceArea")
  public List<VendorProfileRequest> fetchVendorsByServiceArea(String serviceArea) {
    var vendorsList = repoManager.getVendorRepo().findByServiceArea(serviceArea);
    return vendorsList.stream()
        .map(vendor -> modelMapper.map(vendor, VendorProfileRequest.class))
        .toList();
  }

  @Cacheable(value = CacheType.CACHE_TYPE_VENDORS, key = "#vendorId")
  public Vendor fetchVendor(final Long vendorId) {
    return repoManager
        .getVendorRepo()
        .findById(vendorId)
        .orElseThrow(
            () ->
                new APIException(
                    APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid vendor id " + vendorId));
  }

  public PaginationResponse<VendorBasicDTO> fetchActiveVendorsByServiceArea(
      String zipCode, Long categoryId, Integer pageNumber, Integer pageSize) {
    Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
    log.debug("Fetch vendors for service_area : {}", zipCode);
    if (categoryId == null)
      return processPaginationResult(
          repoManager.getVendorRepo().findActiveVendorsByZipCode(zipCode, pageDetails));
    else
      return processPaginationResult(
          repoManager
              .getVendorRepo()
              .findActiveVendorsByZipCodeAndCategory(zipCode, categoryId, pageDetails));
  }

  public PaginationResponse<VendorBasicDTO> fetchActiveVendorsByZipCodeAndProduct(
      String zipCode, Long productId, Integer pageNumber, Integer pageSize) {
    Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
    log.debug("Fetch vendors for product : {}", productId);
    return processPaginationResult(
        repoManager.getVendorRepo().findActiveVendorsByZipCode(zipCode, pageDetails));
  }

  private PaginationResponse<VendorBasicDTO> processPaginationResult(Page<Object[]> pageResult) {
    List<Object[]> pageStores = pageResult.getContent();
    List<VendorBasicDTO> vendorsList = new ArrayList<>();

    for (Object[] result : pageStores) {
      Long vendorId = ((Number) result[0]).longValue();
      String businessName = (String) result[1];
      String bannerImage = (String) result[2];
      String serviceArea = (String) result[3];

      // Cast the category array to a list
      String[] categoriesArray = (String[]) result[4];
      List<String> categories = Arrays.asList(categoriesArray);

      // Create VendorCategoryDTO
      VendorBasicDTO vendor =
          new VendorBasicDTO(vendorId, businessName, bannerImage, serviceArea, categories);
      vendorsList.add(vendor);
    }
    return new PaginationResponse<>(
        vendorsList,
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.getTotalPages(),
        pageResult.isLast());
  }

  private Map<String, Object> fetchVendorsByCategory(List<Object[]> rawResults) {
    if (rawResults == null || rawResults.isEmpty()) {
      log.info("No vendors found");
      return Collections.emptyMap();
    }
    List<VendorBasicDTO> vendorList = new ArrayList<>();
    Map<String, List<VendorBasicDTO>> categoryMap = new HashMap<>();

    for (Object[] result : rawResults) {
      Long vendorId = ((Number) result[0]).longValue();
      String businessName = (String) result[1];
      String bannerImage = (String) result[2];
      String serviceArea = (String) result[3];

      // Cast the category array to a list
      String[] categoriesArray = (String[]) result[4];
      List<String> categories = Arrays.asList(categoriesArray);

      // Create VendorCategoryDTO
      VendorBasicDTO vendor =
          new VendorBasicDTO(vendorId, businessName, bannerImage, serviceArea, categories);
      vendorList.add(vendor);

      // Add to map grouped by category
      for (String category : categories) {
        categoryMap.computeIfAbsent(category, k -> new ArrayList<>()).add(vendor);
      }
    }

    // Create response map with both formats
    Map<String, Object> response = new HashMap<>();
    response.put("all_vendors", vendorList);
    response.put("vendors_by_category", categoryMap);
    return response;
  }

  @Cacheable(value = CacheType.CACHE_TYPE_VENDORS, key = "'ALL_VENDORS'")
  public Map<String, Object> fetchVendorsAndGroupedByCategory() {
    log.debug("Fetch all vendors ");
    return fetchVendorsByCategory(repoManager.getVendorRepo().findAllUniqueVendorsWithCategories());
  }

  public final void addVendorLegalDetails(
      Long vendorId, LegalDetailsDTO detailsDTO, Authentication authentication) {
    log.debug(
        "Adding legal details for vendor #{} by user#{}", vendorId, authentication.getPrincipal());
    Long principal = (Long) authentication.getPrincipal();
    //   var userProfile= getUserService().fetchUserById(principal);
    var roles = getRoles((UserAuthentication) authentication);
    log.debug("User ID #{} and Assigned Roles :{}", principal, roles);
    boolean isAdmin = roles.contains(UserRoleEnum.ADMIN.name());
    boolean isCustomerCare = roles.contains(UserRoleEnum.CUSTOMER_CARE.name());
    if (isAdmin || isCustomerCare) {
      addVendorLegalDetails(vendorId, detailsDTO);
    } else {
      throw new APIException(
          APIErrorCode.UN_AUTHORIZED_ACCESS,
          String.format(
              "User %s not allowed to add legal details to vendor profile: %s",
              principal, vendorId));
    }
  }

  public List<CategoryProjection> fetchAssignedCategories(Long vendorId) {
    log.debug("Fetching assigned categories for vendor : {}", vendorId);
    return getRepoManager().getVendorCategoryRepo().findCategoriesByVendorId(vendorId);
  }

  public List<ProductProjection> fetchAssignedProducts(Long vendorId) {
    log.debug("Fetching assigned products for vendor : {}", vendorId);
    return getRepoManager().getVendorProductRepo().findProductsByVendor(vendorId);
  }

  /**
   * This method fetch SKUs by vendor product ID.
   *
   * @param vendorId
   * @param productId
   * @param pageNumber
   * @param pageSize
   * @return
   */
  public PaginationResponse<SkuDTO> fetchSkusByVendorProductId(
      Long vendorId, Long productId, Integer pageNumber, Integer pageSize) {
    log.debug("Fetching skus for vendor id {} and product id: {}", vendorId, productId);
    Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
    Page<Object[]> pageResult = null;
    if (productId == null) {
      pageResult = getRepoManager().getSkuRepo().findSkusByVendorId(vendorId, pageDetails);
    } else {
      pageResult = getRepoManager().getSkuRepo().findSkusByVendorProductId(productId, pageDetails);
    }
    List<Object[]> queryResults = pageResult.getContent();
    List<SkuDTO> productSkus =
        queryResults.stream()
            .map(
                result -> {
                  try {
                    var skuDto =
                        SkuDTO.builder()
                            .vendorProductId((Long) result[0])
                            .skuId((Long) result[1])
                            .skuName((String) result[2])
                            .imagePath((String) result[3])
                            .skuSize((String) result[4])
                            .skuType(SkuType.valueOf((String) result[5]))
                            .active((Boolean) result[6])
                            .validDays((Integer) result[7])
                            .priceId((Long) result[8])
                            .listPrice(((BigDecimal) result[9]).doubleValue())
                            .salePrice(
                                (result[10]) != null
                                    ? ((BigDecimal) result[10]).doubleValue()
                                    : 0) // salePrice)
                            .effectiveDate(((Date) result[11]).toLocalDate())
                            .eligibleSubscriptionDetails(
                                new ObjectMapper()
                                    .readValue(
                                        (String) result[12],
                                        new TypeReference<List<SkuDTO.SubscriptionDetails>>() {}))
                            .build();
                    skuDto.setOnSale();
                    skuDto.setDiscount();
                    return skuDto;
                  } catch (Exception e) {
                    log.error("Unable to parse SQL result into ProductSkuDTO  :", e);
                    throw new RuntimeException(e);
                  }
                })
            .toList();
    return new PaginationResponse<>(
        productSkus,
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.getTotalPages(),
        pageResult.isLast());
  }
}
