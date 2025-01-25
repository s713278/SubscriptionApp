package com.app.services;

import com.app.config.AppConstants;
import com.app.entites.*;
import com.app.entites.type.DeliveryMode;
import com.app.entites.type.SkuType;
import com.app.entites.type.SubFrequency;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.response.ItemSubscriptionResponse;
import com.app.payloads.response.ServiceSubscriptionResponse;
import com.app.payloads.response.SubscriptionCreateResponse;
import com.app.repositories.RepositoryManager;
import com.app.repositories.projections.VendorProjection;
import com.app.services.constants.PaymentType;
import com.app.services.validator.SubscriptionValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Primary
@Slf4j
public class DefaultSubscriptionCreateService extends AbstractSubscriptionCreateService {

  public DefaultSubscriptionCreateService(
      ServiceManager serviceManager,
      RepositoryManager repoManager,
      SubscriptionServiceHelper serviceHelper) {

    super(serviceManager, repoManager, serviceHelper);
  }

  @Async
  @Override
  protected void updateInventory(Long skuId, Integer quantity) {}

  @Override
  protected void validateSubscription(
      CreateSubscriptionRequest request,
      Sku sku,
      SkuSubscriptionPlan skuSubPlan,
      Customer customer) {
    var skuType = sku.getSkuType();
    // Validate if SKU is active
    isSkuAvailable(sku);

    // Check for duplicate subscription
    checkForSameSubscription(customer.getId(), request);

    // Validate subscription plan
    validateSubFrequencyDetails(skuSubPlan, request);

    // Validate
    validateSubscriptionPlanDates(
        skuType, skuSubPlan.getSubscriptionPlan().getFrequency(), request);
    // Validate price consistency
    validatePrice(request);

    // Validate customer's delivery address
    validateAddress(skuType, customer);
  }

  @Override
  protected SubscriptionCreateResponse mapToResponseDTO(
      Subscription subscription, Sku sku, Customer customer, VendorProjection vendor) {
    SubscriptionCreateResponse response = null;
    switch (sku.getSkuType()) {
      case SERVICE -> {
        ServiceSubscriptionResponse response1 = new ServiceSubscriptionResponse();
        var skuServiceAttributes = sku.getServiceAttributes();
        response1.setStartDate(subscription.getStartDate());
        response1.setExpirationDate(subscription.getEndDate());
        response1.setServiceValidity(skuServiceAttributes.getValidDays());
        response1.setNoOfUses(skuServiceAttributes.getNoOfUses());
        response1.setServiceLocation(Map.of("address", "address1"));
        response = response1;
      }
      case ITEM -> {
        ItemSubscriptionResponse response2 = new ItemSubscriptionResponse();
        response2.setDeliveryAddress(customer.getDeliveryAddress());
        response2.setQuantity(subscription.getQuantity());
        response2.setNextDeliveryDate(subscription.getNextDeliveryDate());
        response = response2;
      }
      default -> throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "");
    }
    response.setStatus(subscription.getStatus());
    response.setSubscriptionId(subscription.getId());
    response.setCreateDate(subscription.getCreatedDate());
    var subPlan = subscription.getSubscriptionPlan();
    response.setFrequency(subPlan.getFrequency());
    response.setDeliveryMode(subPlan.getDeliveryMode());
    response.setPaymentMethod(PaymentType.CASH_ON_DELIVERY.name());
    response.setSkuType(sku.getSkuType());
    response.setVendorName(vendor.getBusinessName());
    response.setMobileNumber(customer.getFullMobileNumber());
    response.setSkuName(sku.getName());
    return response;
  }

  private void checkForSameSubscription(Long userId, CreateSubscriptionRequest request) {
    var optionalSubscription =
        getServiceManager()
            .getSubscriptionService()
            .fetchByUserIdAndSkuId(userId, request.getSkuId());
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
  private void validateSubFrequencyDetails(
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
  }

  private void validateSubscriptionPlanDates(
      SkuType skuType, SubFrequency frequency, CreateSubscriptionRequest request) {
    if (SkuType.SERVICE == skuType) {
      log.debug(
          "Skipping start date ,end date or delivery dates validation for SERVICE SKU {}",
          request.getSkuId());
      return;
    }
    switch (frequency) {
      case ONE_TIME -> {
        // DeliveryDate is required
        SubscriptionValidator.validateDeliveryDate(request.getDeliveryDate());
      }
      case DAILY -> {
        SubscriptionValidator.validateStartDate(request.getStartDate());
        // Start Date is required
      }
      case CUSTOM -> {
        // Start Date is required
        // Custom days required
        SubscriptionValidator.validateStartDate(request.getStartDate());
        SubscriptionValidator.validateCustomDays(request.getCustomDays());
      }
      default -> {}
    }
  }

  /**
   * This method validates price whether is there any price change with user selection and vendor
   * price change on subscription created date.
   *
   * @param request
   */
  private void validatePrice(CreateSubscriptionRequest request) {
    log.debug("Validating is there any price change for sku :{}", request.getSkuId());
    var dbPriceId =
        getServiceManager().getPriceService().fetchTodayPriceBySkuId(request.getSkuId());
    if (!Objects.equals(request.getSkuId(), dbPriceId.getSku().getId())) {
      log.error(
          "Price id not belongs to {} for sku id {}", request.getPriceId(), request.getSkuId());
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
          String.format(
              "Price id not belongs to %s for sku id %s",
              request.getPriceId(), request.getSkuId()));
    }
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

  private void isSkuAvailable(Sku sku) {
    log.debug(
        "Validating whether the SKU : {} is available or not ? {} ", sku.getId(), sku.isActive());
    if (!sku.isActive()) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
          String.format("SKU %s is not available now", sku.getId()));
    }
  }
}
