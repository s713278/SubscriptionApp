package com.app.services.impl;

import com.app.constants.CacheType;
import com.app.constants.NotificationType;
import com.app.entites.Customer;
import com.app.entites.Vendor;
import com.app.entites.VendorCategory;
import com.app.entites.VendorLegalDetails;
import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.UserRoleEnum;
import com.app.entites.type.VendorStatus;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.LegalDetailsDTO;
import com.app.payloads.request.AssignCategoriesRequest;
import com.app.payloads.request.VendorProfileRequest;
import com.app.payloads.response.CreateItemResponse;
import com.app.payloads.response.PaginationResponse;
import com.app.payloads.response.VendorProfileResponse;
import com.app.repositories.RepositoryManager;
import com.app.services.auth.dto.UserAuthentication;
import com.app.services.notification.NotificationContext;
import com.app.services.notification.NotificationTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    var vendor = fetchFullVendorDetailsById(vendorId);
    if (!Objects.equals(vendor.getUserId(), userPrincipal)) {
      throw new APIException(
          APIErrorCode.API_403, "Unauthorized access to fetch vendor profile " + vendorId);
    }
    return modelMapper.map(vendor, VendorProfileResponse.class);
  }

  protected VendorProfileResponse fetchFullVendorDetailsById(Long vendorId) {
    var vendor =
        repoManager
            .getVendorRepo()
            .findById(vendorId)
            .orElseThrow(
                () ->
                    new APIException(
                        APIErrorCode.API_404,
                        "Vendor profile not existed for vendor #" + vendorId));

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
    assignCategories(vendorEntity.getId(), vendorRequest.getAssignCategories());
    return new CreateItemResponse(vendorEntity.getId(), "Vendor profile created successfully.");
  }

  protected void preCreateVendorProfile(VendorProfileRequest vendorRequest) {
    // TODO: Add any pre create validation
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
  public VendorProfileResponse fetchFullVendorDetailsById(
      Long vendorId, Authentication authentication) {
    UserAuthentication userAuthentication = (UserAuthentication) authentication;
    Long userId = (Long) userAuthentication.getPrincipal();
    var roles = getRoles(userAuthentication);
    log.debug("User #{} roles :{}", userId, roles);
    boolean isAdmin = roles.contains(UserRoleEnum.ADMIN.name());
    boolean isVendor = roles.contains(UserRoleEnum.VENDOR.name());
    boolean isCustomerCare = roles.contains(UserRoleEnum.CUSTOMER_CARE.name());
    var vendorDetails = fetchFullVendorDetailsById(vendorId);
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
          APIErrorCode.API_404, "Vendor details not defined in system for user " + vendorId);
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
          APIErrorCode.API_404, "Vendor details not defined in system for user " + vendorId);
    }
    getRepoManager().getVendorRepo().updateVendorStatus(vendorId, vendorStatus);
  }

  public PaginationResponse<VendorProfileRequest> fetchAllVendors(
      Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
    Sort sortByAndOrder =
        sortOrder.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
    Page<Vendor> pageStores = repoManager.getVendorRepo().findAll(pageDetails);
    List<Vendor> stores = pageStores.getContent();
    if (stores.isEmpty()) {
      throw new APIException(
          APIErrorCode.BAD_REQUEST_RECEIVED, "No registered vendors found in database!!");
    }
    List<VendorProfileRequest> storeDTOs =
        stores.stream()
            .map(store -> modelMapper.map(store, VendorProfileRequest.class))
            .collect(Collectors.toList());
    return new PaginationResponse<>(
        storeDTOs,
        pageStores.getNumber(),
        pageStores.getSize(),
        pageStores.getTotalElements(),
        pageStores.getTotalPages(),
        pageStores.isLast());
  }

  @Cacheable(value = CacheType.CACHE_TYPE_VENDORS, key = "#mobileId")
  public VendorProfileResponse fetchVendorByMobile(String mobileId, Authentication authentication) {
    log.debug(
        "Fetching vendor profile for mobile #{} buy user #{}",
        mobileId,
        authentication.getPrincipal());
    UserAuthentication userAuthentication = (UserAuthentication) authentication;
    Long userId = (Long) userAuthentication.getPrincipal();
    var roles = getRoles(userAuthentication);
    boolean isAdmin = roles.contains(UserRoleEnum.ADMIN.name());
    boolean isVendor = roles.contains(UserRoleEnum.VENDOR.name());
    boolean isCustomerCare = roles.contains(UserRoleEnum.CUSTOMER_CARE.name());
    var optionalVendor = repoManager.getVendorRepo().findByContactNumber(String.valueOf(mobileId));
    if (optionalVendor.isEmpty()) {
      throw new APIException(
          APIErrorCode.API_404, "Vendor not existed with this number #" + mobileId);
    }
    var vendorDetails = modelMapper.map(optionalVendor.get(), VendorProfileResponse.class);
    if (isAdmin) {
      return vendorDetails;
    }
    if (isCustomerCare) {
      vendorDetails.setPANNumber("");
      vendorDetails.setGSTNumber("");
      vendorDetails.setRegNumber("");
      return vendorDetails;
    } else if (isVendor) {
      return validateOwnershipAndGet(optionalVendor.get().getId(), userId);
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

  @Async
  @Transactional
  public void assignCategories(Long vendorId, AssignCategoriesRequest request) {
    // Validate input
    if (request == null || request.categoryIds() == null || request.categoryIds().length == 0) {
      throw new IllegalArgumentException("Category IDs cannot be null or empty.");
    }

    // Fetch existing categories for the vendor to avoid duplicates
    Set<Long> existingCategoryIds =
        getRepoManager().getVendorCategoryRepo().findByVendorId(vendorId).stream()
            .map(VendorCategory::getCategoryId)
            .collect(Collectors.toSet());

    // Filter out categories that are already assigned
    List<VendorCategory> newVendorCategories =
        Arrays.stream(request.categoryIds())
            .filter(catId -> !existingCategoryIds.contains(catId))
            .map(catId -> new VendorCategory(catId, vendorId))
            .collect(Collectors.toList());

    // Save new categories in bulk for better performance
    if (!newVendorCategories.isEmpty()) {
      getRepoManager().getVendorCategoryRepo().saveAll(newVendorCategories);
      log.info(
          "Assigned {} new categories to vendor with ID: {}", newVendorCategories.size(), vendorId);
    } else {
      log.info("No new categories to assign for vendor with ID: {}", vendorId);
    }
  }
}
