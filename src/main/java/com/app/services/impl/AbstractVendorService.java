package com.app.services.impl;

import com.app.constants.CacheType;
import com.app.constants.NotificationType;
import com.app.entites.*;
import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.UserRoleEnum;
import com.app.entites.type.VendorStatus;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.LegalDetailsDTO;
import com.app.payloads.request.AssignCategoriesRequest;
import com.app.payloads.request.AssignProductsRequest;
import com.app.payloads.request.VendorProfileRequest;
import com.app.payloads.response.CreateItemResponse;
import com.app.payloads.response.PaginationResponse;
import com.app.payloads.response.VendorProfileResponse;
import com.app.repositories.RepositoryManager;
import com.app.services.auth.dto.UserAuthentication;
import com.app.services.notification.NotificationContext;
import com.app.services.notification.NotificationTemplate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

@Setter
@Getter
@Slf4j
public abstract class AbstractVendorService {

  protected ModelMapper modelMapper;
  protected RepositoryManager repoManager;
  protected UserService userService;
  protected NotificationContext notificationContext;

  protected VendorProfileResponse validateOwnershipAndGet(Long vendorId, Long userPrincipal) {
    var vendor = fetchVendorById(vendorId);
    if (!Objects.equals(vendor.getUserId(), userPrincipal)) {
      throw new APIException(
          APIErrorCode.API_403, "Unauthorized access to fetch vendor profile " + vendorId);
    }
    return modelMapper.map(vendor, VendorProfileResponse.class);
  }

  protected VendorProfileResponse fetchVendorById(Long vendorId) {
    var vendor =
        repoManager
            .getVendorRepo()
            .findById(vendorId)
            .orElseThrow(
                () ->
                    new APIException(
                        APIErrorCode.ENTITY_NOT_FOUND,
                        "Vendor profile not existed for with ID #" + vendorId));

    return modelMapper.map(vendor, VendorProfileResponse.class);
  }

  protected Set<String> getRoles(UserAuthentication userAuthentication) {
    if (userAuthentication.getAuthorities().isEmpty()) {
      throw new APIException(
          APIErrorCode.API_403,
          "User #" + userAuthentication.getPrincipal() + "have no access to fetch vendor profile ");
    }
    return userAuthentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());
  }

  @Transactional
  public final CreateItemResponse createVendorProfile(
      VendorProfileRequest vendorRequest, Authentication authentication) {
    preCreateVendorProfile(vendorRequest);
    UserAuthentication userAuthentication = (UserAuthentication) authentication;
    Long principal = (Long) userAuthentication.getPrincipal();
    //   var userProfile= getUserService().fetchUserById(principal);
    var roles = getRoles(userAuthentication);
    log.debug("User ID #{} and Assigned Roles :{}", principal, roles);
    boolean isAdmin = roles.contains(UserRoleEnum.ADMIN.name());
    boolean isVendor = roles.contains(UserRoleEnum.VENDOR.name());
    boolean isCustomerCare = roles.contains(UserRoleEnum.CUSTOMER_CARE.name());
    Vendor vendor = getModelMapper().map(vendorRequest, Vendor.class);
    if (isAdmin || isCustomerCare) { // User and Vendor Profile has to be created
      vendor.setCreatedBy("" + principal);
      vendor.setStatus(VendorStatus.ACTIVE);
      vendor.setApprovalStatus(ApprovalStatus.APPROVED);
      Customer customer = new Customer();
      customer.setCountryCode("+91");
      customer.setMobile(Long.parseLong(vendorRequest.getContactNumber()));
      customer.setCreatedBy(principal + "");
      var vendorEntity = getUserService().createUser(customer);
      vendor.setUserId(vendorEntity.getId());
      postCreateVendorProfile(vendorEntity.getId(), customer.getFullMobileNumber());
    } else if (isVendor) { // Vendor Profile has to be created
      // Check if the user has already vendor profile
      Long vendorId = getRepoManager().getVendorRepo().findByUserId(principal);
      // fetchVendorById(principal)
      if (vendorId != null) {
        throw new APIException(
            APIErrorCode.DUPLICATE_REQUEST, "User has already vendor profile with id :" + vendorId);
      }
      vendor.setUserId(principal);
      vendor.setStatus(VendorStatus.INACTIVE);
      vendor.setApprovalStatus(ApprovalStatus.PENDING);
      // return validateOwnershipAndGet(vendorId,userId);
    } else {
      throw new APIException(
          APIErrorCode.API_401,
          String.format("User %s not allowed to have vendor profile", principal));
    }
    Vendor vendorEntity = getRepoManager().getVendorRepo().save(vendor);
    if (isAdmin || isCustomerCare) {
      assignCategories(vendorEntity.getId(), vendorRequest.getAssignCategories());
    }
    return new CreateItemResponse(vendorEntity.getId(), "Vendor profile created successfully.");
  }

  protected void preCreateVendorProfile(VendorProfileRequest vendorRequest) {
    var optionalVendor =
        repoManager.getVendorRepo().findByContactNumber(vendorRequest.getContactNumber());
    if (optionalVendor.isPresent()) {
      log.error(
          "Vendor profile already existed for this mobile #"
              + vendorRequest.getContactNumber()
              + " with profile ID #"
              + optionalVendor.get().getId());
      throw new APIException(
          APIErrorCode.BAD_REQUEST_RECEIVED,
          "Vendor profile already existed for this mobile #"
              + vendorRequest.getContactNumber()
              + " with profile ID #"
              + optionalVendor.get().getId());
    }
  }

  @Async
  protected void postCreateVendorProfile(Long vendorId, String mobileNumber) {
    log.info("Inviting vendor :{} to complete to complete onboarding process.", vendorId);
    notificationContext.notifyUser(
        NotificationType.SMS, NotificationTemplate.PROFILE_UPDATES, mobileNumber);
  }

  @Transactional
  public final CreateItemResponse updateVendorProfile(
      VendorProfileRequest vendorRequest, Long vendorId, Authentication authentication) {
    log.debug("Start - Updating vendor profile {} by {}", vendorId, authentication.getPrincipal());
    Vendor existedProfile = preUpdateVendorProfile(vendorRequest, vendorId);
    UserAuthentication userAuthentication = (UserAuthentication) authentication;
    Long principal = (Long) userAuthentication.getPrincipal();

    var userProfile = getUserService().fetchUserById(principal);
    var roles = getRoles(userAuthentication);
    boolean isAdmin = roles.contains(UserRoleEnum.ADMIN.name());
    boolean isVendor = roles.contains(UserRoleEnum.VENDOR.name());
    boolean isCustomerCare = roles.contains(UserRoleEnum.CUSTOMER_CARE.name());
    Vendor vendor = getModelMapper().map(vendorRequest, Vendor.class);
    if (isAdmin || isCustomerCare) { // User and Vendor Profile has to be created
      // existedProfile.setStatus();
      // postCreateVendorProfile(existedProfile.getId(),customer.getFullMobileNumber());
    } else if (isVendor) { // Vendor Profile ownership authentication
      validateOwnershipAndGet(vendorId, principal);
    } else {
      throw new APIException(
          APIErrorCode.API_401,
          String.format("User %s not allowed to have vendor profile", principal));
    }
    Vendor vendorEntity = getRepoManager().getVendorRepo().save(existedProfile);
    return new CreateItemResponse(
        vendorEntity.getId(),
        String.format("Vendor profile:%s updated by %s successfully.", vendorId, principal));
  }

  protected Vendor preUpdateVendorProfile(VendorProfileRequest vendorRequest, Long vendorId) {
    log.debug("Validating vendor id {}", vendorId);
    var vendor = getRepoManager().getVendorRepo().findById(vendorId);
    if (vendor.isEmpty()) {
      throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Vendor profile is not existed");
    }
    if (VendorStatus.SUSPENDED == vendor.get().getStatus()) {
      throw new APIException(
          APIErrorCode.BAD_REQUEST_RECEIVED,
          "Vendor profile is suspended state,No further updates allowed.");
    }
    return vendor.get();
  }

  @Cacheable(value = CacheType.CACHE_TYPE_VENDORS, key = "#vendorId")
  public VendorProfileResponse fetchVendorById(Long vendorId, Authentication authentication) {
    UserAuthentication userAuthentication = (UserAuthentication) authentication;
    Long userId = (Long) userAuthentication.getPrincipal();
    var roles = getRoles(userAuthentication);
    log.debug("User #{} roles :{}", userId, roles);
    boolean isAdmin = roles.contains(UserRoleEnum.ADMIN.name());
    boolean isVendor = roles.contains(UserRoleEnum.VENDOR.name());
    boolean isCustomerCare = roles.contains(UserRoleEnum.CUSTOMER_CARE.name());
    var vendorDetails = fetchVendorById(vendorId);
    if (isAdmin) {
      return vendorDetails;
    }
    if (isCustomerCare) {
      vendorDetails.setPANNumber("");
      vendorDetails.setGSTNumber("");
      vendorDetails.setRegNumber("");
      return vendorDetails;
    } else if (isVendor) {
      return validateOwnershipAndGet(vendorId, userId);
    }
    log.warn("User #{} doesn't have access to view vendor profile #{}", userId, vendorId);
    throw new APIException(APIErrorCode.API_403, "Unauthorized to fetch vendor profile");
  }

  @Transactional(readOnly = false)
  public void updateApprovalStatus(Long vendorId, ApprovalStatus approvalStatus) {
    log.debug("DB update request for vendor's approval status of user_id : {} ", vendorId);
    var isExisted = getRepoManager().getVendorRepo().existsById(vendorId);
    if (!isExisted) {
      throw new APIException(
          APIErrorCode.ENTITY_NOT_FOUND,
          "Vendor details not defined in system for user " + vendorId);
    }
    VendorStatus vendorStatus = VendorStatus.INACTIVE;
    if (Objects.requireNonNull(approvalStatus) == ApprovalStatus.APPROVED) {
      vendorStatus = VendorStatus.ACTIVE;
    }
    getRepoManager().getVendorRepo().updateApprovalStatus(vendorId, approvalStatus, vendorStatus);
  }

  @Transactional(readOnly = false)
  public void updateVendorStatus(Long vendorId, VendorStatus vendorStatus) {
    log.debug("DB update request for vendor's status of user_id : {} ", vendorId);
    var isExisted = getRepoManager().getVendorRepo().existsById(vendorId);
    if (!isExisted) {
      throw new APIException(
          APIErrorCode.ENTITY_NOT_FOUND,
          "Vendor details not defined in system for user " + vendorId);
    }
    getRepoManager().getVendorRepo().updateVendorStatus(vendorId, vendorStatus);
  }

  public PaginationResponse<VendorProfileResponse> fetchAllVendors(
      Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
    Sort sortByAndOrder =
        sortOrder.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
    Page<Vendor> pageStores = repoManager.getVendorRepo().findAll(pageDetails);
    List<Vendor> stores = pageStores.getContent();
    List<VendorProfileResponse> storeDTOs =
        stores.stream()
            .map(store -> modelMapper.map(store, VendorProfileResponse.class))
            .collect(Collectors.toList());
    return new PaginationResponse<>(
        storeDTOs,
        pageStores.getNumber(),
        pageStores.getSize(),
        pageStores.getTotalElements(),
        pageStores.getTotalPages(),
        pageStores.isLast());
  }

  @Cacheable(value = CacheType.CACHE_TYPE_VENDORS, key = "#mobileNo")
  public VendorProfileResponse fetchVendorByMobile(String mobileNo, Authentication authentication) {
    log.debug(
        "Fetching vendor profile for mobile #{} buy user #{}",
        mobileNo,
        authentication.getPrincipal());
    UserAuthentication userAuthentication = (UserAuthentication) authentication;
    Long userId = (Long) userAuthentication.getPrincipal();
    var roles = getRoles(userAuthentication);
    boolean isAdmin = roles.contains(UserRoleEnum.ADMIN.name());
    boolean isVendor = roles.contains(UserRoleEnum.VENDOR.name());
    boolean isCustomerCare = roles.contains(UserRoleEnum.CUSTOMER_CARE.name());
    Vendor vendor =
        repoManager
            .getVendorRepo()
            .findByContactNumber(mobileNo)
            .orElseThrow(
                () ->
                    new APIException(
                        APIErrorCode.ENTITY_NOT_FOUND,
                        "No registered vendor profile existed with this mobile no#" + mobileNo));
    var vendorDetails = modelMapper.map(vendor, VendorProfileResponse.class);
    if (isAdmin) {
      return vendorDetails;
    }
    if (isCustomerCare) {
      vendorDetails.setPANNumber("");
      vendorDetails.setGSTNumber("");
      vendorDetails.setRegNumber("");
      return vendorDetails;
    } else if (isVendor) {
      return validateOwnershipAndGet(vendor.getId(), userId);
    }
    throw new APIException(APIErrorCode.API_403, "Unauthorized to fetch vendor profile");
  }

  @Transactional(readOnly = false)
  protected void addVendorLegalDetails(Long vendorId, LegalDetailsDTO detailsDTO) {
    log.info("Adding legal details for vendor #{}", vendorId);
    getRepoManager()
        .getVendorRepo()
        .findById(vendorId)
        .ifPresent(
            (vendor) -> {
              var vendorLegalEntity = getModelMapper().map(detailsDTO, VendorLegalDetails.class);
              vendorLegalEntity.setVendor(vendor);
              getRepoManager().getVendorLegalDetailsRepo().save(vendorLegalEntity);
            });
  }

  @Transactional
  public void assignCategories(Long vendorId, AssignCategoriesRequest request) {
    log.debug(
        "Assign categories to vendor  #{} and category ids :#{}", vendorId, request.categoryIds());
    //  Step 1:Validate input
    if (request.categoryIds() == null || request.categoryIds().isEmpty()) {
      throw new APIException(
          APIErrorCode.BAD_REQUEST_RECEIVED, "Category IDs cannot be null or empty.");
    }
    // Step 2: Validate the vendor ID
    Vendor vendor =
        getRepoManager()
            .getVendorRepo()
            .findById(vendorId)
            .orElseThrow(
                () ->
                    new APIException(
                        APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid vendor ID: " + vendorId));
    // Step 3: Validate the categories whether those are existed in category table or not.
    var validCategories =
        getRepoManager()
            .getCategoryRepo()
            .findValidCategories(request.categoryIds())
            .orElseThrow(
                () ->
                    new APIException(
                        APIErrorCode.BAD_REQUEST_RECEIVED, "All Category IDs are invalid."));
    log.debug(
        "Vendor eligible categories {} but the requested categories {} ",
        validCategories,
        request.categoryIds());
    if (validCategories.size() != request.categoryIds().size()) {
      throw new IllegalArgumentException(
          "The following one or more category IDs are invalid" + request.categoryIds());
    }

    // Step 4: Validate product IDs against VendorProduct entity (already assigned check)
    Set<Long> alreadyAssignedCategoryIds =
        getRepoManager()
            .getVendorCategoryRepo()
            .findAssignedCategoryIDsForVendor(vendorId, validCategories);
    if (!alreadyAssignedCategoryIds.isEmpty()) {
      throw new IllegalArgumentException(
          "The following categories are already assigned: " + alreadyAssignedCategoryIds);
    }

    // Step 5: Prepare batch updates
    List<VendorCategory> newVendorCategories =
        validCategories.stream()
            .map(catId -> new VendorCategory(catId, vendorId))
            .collect(Collectors.toList());
    getRepoManager().getVendorCategoryRepo().saveAll(newVendorCategories);
    log.info(
        "Assign categories to vendor  #{} and category ids :#{} is success.",
        vendorId,
        validCategories);
  }

  @Transactional
  public void assignProducts(Long vendorId, Map<Long, List<AssignProductsRequest>> request) {
    // Step 1: Validate the vendor ID
    Vendor vendor =
        getRepoManager()
            .getVendorRepo()
            .findById(vendorId)
            .orElseThrow(
                () ->
                    new APIException(
                        APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid vendor ID: " + vendorId));

    // Step 2: Extract all product IDs from the request
    Set<Long> productIds =
        request.values().stream()
            .flatMap(List::stream)
            .map(AssignProductsRequest::productId)
            .collect(Collectors.toSet());

    Long categoryId = request.keySet().stream().findFirst().get();
    // Step 3: Validate product IDs whether all the productId are same Category or not
    Set<Long> validProductIds =
        getRepoManager()
            .getProductRepo()
            .findValidProductIDs(categoryId, productIds)
            .orElseThrow(
                () ->
                    new APIException(
                        APIErrorCode.BAD_REQUEST_RECEIVED,
                        "All product IDs are invalid.No product id is belongs to category id #"
                            + categoryId));

    if (validProductIds.size() != productIds.size()) {
      throw new IllegalArgumentException(
          "One or more product IDs are invalid or might be not assigned to category #"
              + categoryId);
    }

    // Step 4: Validate product IDs against VendorProduct entity (already assigned check)
    Set<Long> alreadyAssignedProductIds =
        getRepoManager()
            .getVendorProductRepo()
            .validateAssignedProductIdsForVendor(vendorId, validProductIds);
    if (!alreadyAssignedProductIds.isEmpty()) {
      throw new IllegalArgumentException(
          "The following products are already assigned: " + alreadyAssignedProductIds);
    }

    // Step 5: Prepare batch updates
    List<VendorProduct> vendorProducts = new ArrayList<>();
    for (Map.Entry<Long, List<AssignProductsRequest>> entry : request.entrySet()) {
      // Long categoryId = entry.getKey();
      List<AssignProductsRequest> productRequests = entry.getValue();
      for (AssignProductsRequest productRequest : productRequests) {
        VendorProduct vendorProduct = new VendorProduct();
        vendorProduct.setVendorId(vendor.getId());
        vendorProduct.setProductId(productRequest.productId());
        vendorProduct.setFeatures(productRequest.features()); // Assuming features is stored as JSON
        vendorProducts.add(vendorProduct);
      }
    }
    // Step 6: Save in batch
    getRepoManager().getVendorProductRepo().saveAll(vendorProducts);
    log.info(
        "Assign products to vendor  #{} and products ids :#{} is success.",
        vendorId,
        validProductIds);
  }
}
