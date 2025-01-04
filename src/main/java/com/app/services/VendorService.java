package com.app.services;

import com.app.entites.VendorLegalDetails;
import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.VendorStatus;
import com.app.payloads.request.AssignCategoriesRequest;
import com.app.payloads.request.AssignProductsRequest;
import com.app.payloads.request.VendorProfileRequest;
import com.app.payloads.response.CreateItemResponse;
import com.app.payloads.response.PaginationResponse;
import com.app.payloads.response.VendorProfileResponse;
import com.app.repositories.projections.CategoryProjection;
import java.util.List;
import java.util.Map;

public interface VendorService {

  CreateItemResponse createVendorProfile(VendorProfileRequest vendorRequest);

  VendorProfileResponse fetchVendorProfileById(Long vendorId);

  VendorProfileResponse fetchVendorProfileByMobile(String mobile);

  void updateVendorProfile(Long vendorId, VendorProfileRequest vendorRequest);

  void updateServiceAreas(Long vendorId, Map<String, Object> serviceAreas);

  void addLegalDetails(Long vendorId, VendorLegalDetails legalDetails);

  void updateVendorStatus(Long vendorId, VendorStatus status);

  void updateApprovalStatus(Long vendorId, ApprovalStatus status);

  // Category management
  void assignCategories(Long vendorId, AssignCategoriesRequest request);

  List<CategoryProjection> fetchAssignedCategories(Long vendorId);

  void unAssignCategories(Long vendorId, Long[] vendorCategoryIds);

  // Product management
  void assignProducts(Long vendorId, AssignProductsRequest vendorProductsRequest);

  void unAssignProducts(Long vendorId, Long[] vendorProductIds);

  void publishVendorApprovalEvent(Long vendorId, ApprovalStatus status);

  default void validateServiceAreas(Map<String, Object> serviceAreas) {
    if (serviceAreas == null || serviceAreas.isEmpty()) {
      throw new IllegalArgumentException("Service areas cannot be null or empty");
    }
  }

  PaginationResponse<VendorProfileRequest> fetchAllVendors(
      Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

  default boolean isValidVendorId(Long vendorId) {
    return vendorId != null && vendorId > 0;
  }
}
