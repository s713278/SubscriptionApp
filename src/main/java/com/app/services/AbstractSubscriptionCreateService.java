package com.app.services;

import com.app.constants.NotificationType;
import com.app.entites.*;
import com.app.entites.type.*;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.response.SubscriptionCreateResponse;
import com.app.repositories.RepositoryManager;
import com.app.repositories.projections.VendorProjection;
import java.time.LocalDate;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Slf4j
public abstract class AbstractSubscriptionCreateService implements SubscriptionCreateService {
  protected ServiceManager serviceManager;
  protected RepositoryManager repoManager;
  protected SubscriptionServiceHelper serviceHelper;

  public AbstractSubscriptionCreateService(
      ServiceManager serviceManager,
      RepositoryManager repoManager,
      SubscriptionServiceHelper serviceHelper) {
    this.serviceManager = serviceManager;
    this.repoManager = repoManager;
    this.serviceHelper = serviceHelper;
  }

  @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
  public final SubscriptionCreateResponse createSubscription(
      Long userId, CreateSubscriptionRequest request) {
    log.info(
        "Start - Create subscription request for user {} with SKU {}", userId, request.getSkuId());
    Sku sku = fetchSku(request.getSkuId());
    SkuSubscriptionPlan skuSubPlan = fetchSubscriptionPlan(request.getSkuSubscriptionPlanId());
    Customer customer = fetchCustomer(userId);
    validateSubscription(request, sku, skuSubPlan, customer);
    var vendor =
        getRepoManager().getVendorProductRepo().findVendorByProductId(sku.getVendorProductId());
    if (vendor.isEmpty()) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
          "SKU " + sku.getName() + " not associated with any vendor");
    }
    // validateVendorStatus(vendor);
    // Fetch customer and tenant info
    // Set SKU, quantity, and frequency
    // Vendor vendor=getServiceManager().getVendorService().fetchVendor(request.getVendorId());
    // validateVendorStatus(vendor);
    //  subscription.setCustomer(customer);
    Subscription subscription = new Subscription();
    subscription.setUserId(userId);
    subscription.setSkuId(request.getSkuId());
    // Get Latest Price from the repository
    subscription.setPriceId(request.getPriceId());
    subscription.setStatus(SubscriptionStatus.PENDING);
    subscription.setQuantity(request.getQuantity());
    subscription.setSubscriptionPlan(skuSubPlan.getSubscriptionPlan());
    // Address
    subscription.setDeliveryAddress(customer.getDeliveryAddress());
    switch (skuSubPlan.getSubscriptionPlan().getFrequency()) {
      case ONE_TIME -> {
        // Update customer validation period
        if (sku.getSkuType() == SkuType.SERVICE) {
          subscription.setStartDate(LocalDate.now());
          var serviceAttributes =
              getServiceManager().getSkuService().fetchServiceAttributesBySkuId(sku.getId());
          subscription.setEndDate(LocalDate.now().plusDays(serviceAttributes.getValidDays()));
          subscription.setQuantity(serviceAttributes.getNoOfUses());
        } else {
          subscription.setStartDate(request.getDeliveryDate());
          subscription.setEndDate(null);
        }
      }
      case CUSTOM -> {
        subscription.setCustomDays(request.getCustomDays());
        subscription.setStartDate(request.getStartDate());
        subscription.setEndDate(request.getEndDate());
      }
      default -> {
        subscription.setStartDate(request.getStartDate());
        subscription.setEndDate(request.getEndDate());
      }
    }
    subscription.setNextDeliveryDate(
        getServiceHelper()
            .calculateNextDeliveryDate(
                skuSubPlan.getSubscriptionPlan().getFrequency(), subscription));
    subscription.setUpdateVersion(1); // Created First Time
    subscription = getRepoManager().getSubscriptionRepo().save(subscription);

    // Create the Data object with subscription_id and next_delivery_date
    SubscriptionCreateResponse responseDTO =
        mapToResponseDTO(subscription, sku, customer, vendor.get());
    //  postSubscription(responseDTO);
    notifyCustomer(customer.getFullMobileNumber(), customer.getEmail());
    notifyVendor(customer.getFullMobileNumber());
    updateInventory(request.getSkuId(), request.getQuantity());
    log.info("End - Create subscription request for user {}", userId);
    return responseDTO;
  }

  protected void validateAddress(SkuType skuType, Customer customer) {
    if (SkuType.SERVICE == skuType) {
      log.debug("Skipping delivery address validation as the SKU type is SERVICE");
      return;
    }
    Map<String, String> deliveryAddress = customer.getDeliveryAddress();
    if (deliveryAddress == null || deliveryAddress.isEmpty()) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED, "Customer doesn't have delivery address.");
    }
  }

  protected void validateVendorStatus(Vendor vendor) {
    if (vendor.getApprovalStatus() != ApprovalStatus.APPROVED) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
          "Vendor " + vendor.getBusinessName() + " is not approved to accept the order.");
    }
    if (vendor.getStatus() != VendorStatus.ACTIVE) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
          "Vendor " + vendor.getBusinessName() + " is not active and willing to take orders.");
    }
  }

  @Async
  protected void notifyCustomer(String mobile, String email) {
    log.debug("Sending user notification to {} ", mobile);
    if (mobile != null) {
      serviceManager.getNotificationContext().sendOTPMessage(NotificationType.SMS, mobile);
    } else if (email != null) {
      serviceManager.getNotificationContext().sendOTPMessage(NotificationType.EMAIL, email);
    } else {
      log.info("Customer has neither mobile number nor email");
    }
  }

  @Async
  protected void notifyVendor(String mobile) {
    log.debug("Sending vendor notification to {} ", mobile);
    getServiceManager().getNotificationContext().sendOTPMessage(NotificationType.SMS, mobile);
  }

  protected abstract void updateInventory(Long skuId, Integer quantity);

  private Sku fetchSku(Long skuId) {
    return getServiceManager().getSkuService().fetchSkuEntityById(skuId);
  }

  private SkuSubscriptionPlan fetchSubscriptionPlan(Long planId) {
    return getServiceManager().getSkuSubscriptionService().fetchSkuSubscriptionById(planId);
  }

  private Customer fetchCustomer(Long userId) {
    return getServiceManager().getUserService().fetchUserById(userId);
  }

  protected abstract void validateSubscription(
      CreateSubscriptionRequest request,
      Sku sku,
      SkuSubscriptionPlan skuSubPlan,
      Customer customer);

  protected abstract SubscriptionCreateResponse mapToResponseDTO(
      Subscription subscription, Sku sku, Customer customer, VendorProjection vendor);
}
