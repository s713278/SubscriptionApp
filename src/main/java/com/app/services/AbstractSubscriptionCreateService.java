package com.app.services;

import com.app.config.AppConstants;
import com.app.constants.NotificationType;
import com.app.entites.*;
import com.app.entites.type.*;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.response.SubscriptionResponseDTO;
import com.app.repositories.RepositoryManager;
import com.app.services.constants.PaymentType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Slf4j
public abstract class AbstractSubscriptionCreateService {
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

  protected void isSkuAvailable(Sku sku) {
    log.debug(
        "Validating whether the SKU : {} is available or not ? {} ", sku.getId(), sku.isActive());
    if (!sku.isActive()) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
          String.format("SKU %s is not available now", sku.getId()));
    }
  }

  protected void checkForSameSubscription(Long userId, CreateSubscriptionRequest request) {
    var optionalSubscription =
        serviceManager.getSubscriptionService().fetchByUserIdAndSkuId(userId, request.getSkuId());
    if (optionalSubscription.isPresent()) {
      if (SubscriptionStatus.EXPIRED != optionalSubscription.get().getStatus()) {
        throw new APIException(
            APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
            String.format(
                "An existing subscription with %s already exists for the same item %s.",
                optionalSubscription.get().getId(), request.getSkuId()));
      }
    }
  }

  /**
   *
   *
   * <ol>
   *   <li>Verifying the duplicate subscription request
   *   <li>Verify the requested frequency is existed in DB or not
   *   <li>Delivery date validation for ONE_TIME frequency
   * </ol>
   *
   * @param request
   */
  protected void validateSubscriptionPlan(
      SkuSubscriptionPlan skuSub, CreateSubscriptionRequest request) {
    if (skuSub.getSubscriptionPlan().getFrequency() == SubFrequency.ONE_TIME) {
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> map = skuSub.getEligibleDeliveryDays();
      List<String> deliveryDaysList =
          objectMapper.convertValue(
              map.get(AppConstants.SUB_ONE_TIME_DELIVERY_DAYS),
              new TypeReference<List<String>>() {});
      log.debug("Sku Eligible Delivery Options : {}", deliveryDaysList);
      if (deliveryDaysList != null
          && !deliveryDaysList.isEmpty()
          && DeliveryMode.FIXED == skuSub.getSubscriptionPlan().getDeliveryMode()) {
        log.debug("Day of the week :{}", request.getDeliveryDate().getDayOfWeek().name());
        if (!deliveryDaysList.contains(request.getDeliveryDate().getDayOfWeek().name())) {
          throw new APIException(
              APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
              "User selected delivery date as "
                  + request.getDeliveryDate().getDayOfWeek().name()
                  + " but its not available for this item : "
                  + request.getSkuId()
                  + ". Please select other day from one of "
                  + skuSub.getEligibleDeliveryDays().get(AppConstants.SUB_ONE_TIME_DELIVERY_DAYS));
        }
      }
    }
    validateSubscriptionPlan(skuSub.getSubscriptionPlan().getFrequency(), request);
  }

  /**
   * This method validates price whether is there any price change with user selection and vendor
   * price change on subscription created date.
   *
   * @param request
   */
  protected void validatePrice(CreateSubscriptionRequest request) {
    log.debug("Validating is there any price change for sku :{}", request.getSkuId());
    var dbPriceId = serviceManager.getPriceService().fetchTodayPriceBySkuId(request.getSkuId());
    if (!dbPriceId.getId().equals(request.getPriceId())) {
      log.warn(
          "Price has been changed to {}  for selected item {}",
          dbPriceId.getSalePrice(),
          request.getSkuId());
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
          String.format("Price has been changed to %s", dbPriceId.getSalePrice()));
    }
  }

  @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
  public final SubscriptionResponseDTO createSubscription(
      Long userId, CreateSubscriptionRequest request) {
    log.info(
        "Start - Create subscription request for user {} with sku {}", userId, request.getSkuId());
    var sku = getServiceManager().getSkuService().fetchSkuEntityById(request.getSkuId());
    var vendor =
        getRepoManager().getVendorProductRepo().findVendorByProductId(sku.getVendorProductId());
    if (vendor.isEmpty()) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
          "SKU " + sku.getName() + " not associated with any vendor");
    }
    isSkuAvailable(sku);
    var skuSubPlan =
        getServiceManager()
            .getSkuSubscriptionService()
            .fetchSkuSubscriptionById(request.getSkuSubscriptionPlanId());
    validateSubscriptionPlan(skuSubPlan, request);
    checkForSameSubscription(userId, request);
    validatePrice(request);
    // Fetch customer and tenant info
    Customer customer = getServiceManager().getUserService().fetchUserById(userId);
    validateAddress(customer.getDeliveryAddress());
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
        if (sku.getSkuType() == SkuType.SERVICE) {
          subscription.setStartDate(LocalDate.now());
          subscription.setEndDate(
              LocalDate.now().plusDays(sku.getServiceAttributes().getValidDays()));
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
    SubscriptionResponseDTO responseDTO =
        new SubscriptionResponseDTO(
            subscription.getId(),
            subscription.getCreatedDate(),
            vendor.get().getBusinessName(),
            subscription.getNextDeliveryDate(),
            PaymentType.CASH_ON_DELIVERY.name(),
            subscription.getStatus(),
            subscription.getSubscriptionPlan().getFrequency(),
            customer.getDeliveryAddress(),
            customer.getFullMobileNumber());
    //  postSubscription(responseDTO);
    notifyCustomer(customer.getFullMobileNumber(), customer.getEmail());
    notifyVendor(customer.getFullMobileNumber());
    updateInventory(request.getSkuId(), request.getQuantity());
    log.info("End - Create subscription request for user {}", userId);
    return responseDTO;
  }

  protected void validateAddress(Map<String, String> deliveryAddress) {
    if (deliveryAddress == null || deliveryAddress.isEmpty()) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED, "Customer doesn't have delivery address.");
    }
  }

  private void validateSubscriptionPlan(SubFrequency frequency, CreateSubscriptionRequest request) {
    switch (frequency) {
      case ONE_TIME -> {
        // DeliveryDate is required
        validateDeliveryDate(request.getDeliveryDate());
      }
      case DAILY -> {
        validateStartDate(request.getStartDate());
        // Start Date is required
      }
      case CUSTOM -> {
        // Start Date is required
        // Custom days required
        validateStartDate(request.getStartDate());
        validateStartDate(request.getCustomDays());
      }
      default -> {}
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
    serviceManager.getNotificationContext().sendOTPMessage(NotificationType.SMS, mobile);
  }

  protected abstract void updateInventory(Long skuId, Integer quantity);

  /**
   * @param date
   */
  private void validateStartDate(LocalDate date) {
    LocalDate today = LocalDate.now();
    LocalDate maxAllowedDate = today.plusDays(AppConstants.SUB_MAX_ALLOWED_START_DATE_DAYS);
    if (date == null) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED, "Start date is required.");
    }
    if (date.isBefore(today) || date.isAfter(maxAllowedDate)) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
          String.format("Start date must be between today (%s) and %s.", today, maxAllowedDate));
    }
  }

  /**
   * @param date
   */
  private void validateDeliveryDate(LocalDate date) {
    LocalDate today = LocalDate.now();
    LocalDate maxAllowedDate = today.plusDays(AppConstants.SUB_MAX_ALLOWED_DELIVERY_DATE_DAYS);
    if (date == null) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED, "Delivery date is required.");
    }
    if (date.isBefore(today) || date.isAfter(maxAllowedDate)) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
          String.format("Delivery date must be between today (%s) and %s.", today, maxAllowedDate));
    }
  }

  private void validateStartDate(List<Integer> customDays) {
    if (customDays == null || customDays.isEmpty()) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED, "Custom days are required.");
    }
  }
}
