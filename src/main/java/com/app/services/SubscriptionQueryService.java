package com.app.services;

import com.app.entites.Subscription;
import com.app.entites.SubscriptionStatus;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.SubscriptionDetailDTO;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.payloads.response.SubscriptionResponse;
import com.app.repositories.RepositoryManager;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionQueryService {

  private final RepositoryManager repoManager;
  private final SubscriptionServiceHelper serviceHelper;
  private final SkuSubscriptionService skuSubscriptionService;

  private void notifyCustomer(Subscription subscription) {
    // Send email or SMS notification to the customer
  }

  public List<Subscription> getSubscriptionsByVendorIdAndCustomerId(
      Long subscriptionId, Long customerId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Transactional
  public SubscriptionResponse updateSubscription(
      Long subId, Long userId, @Valid UpdateSubscriptionRequest request) {
    log.debug("Start - Update subscription request for user {} and sub id {}", userId, subId);
    var subscription =
        repoManager
            .getSubscriptionRepo()
            .findByIdAndUserId(subId, userId)
            .orElseThrow(
                () ->
                    new APIException(
                        APIErrorCode.BAD_REQUEST_RECEIVED,
                        subId
                            + " Subscription with id "
                            + subId
                            + " not existed for user "
                            + userId));

    boolean changedFound = false;
    var entityFrequency = subscription.getSubscriptionPlan().getFrequency();
    if (request.getSkuSubscriptionPlanId() != null
        && !request.getSkuSubscriptionPlanId().equals(subscription.getSubscriptionPlan().getId())) {
      var skuSubPlan =
          skuSubscriptionService
              .fetchSkuSubscriptionById(request.getSkuSubscriptionPlanId())
              .getSubscriptionPlan();
      log.info(
          "Change found in frequency from {} ==>> {} ", entityFrequency, skuSubPlan.getFrequency());
      // subscription.setFrequency(request.getFrequency());
      subscription.setSubscriptionPlan(skuSubPlan);
      changedFound = true;
    }
    if (request.getQuantity() != null
        && !request.getQuantity().equals(subscription.getQuantity())) {
      log.info(
          "Change found in quantity from {} ==>> {} ",
          subscription.getQuantity(),
          request.getQuantity());
      subscription.setQuantity(request.getQuantity());
      changedFound = true;
    }
    if (request.getStartDate() != null
        && !request.getStartDate().equals(subscription.getStartDate())) {
      log.info(
          "Change found in start date from {} ==> {} ",
          subscription.getStartDate(),
          request.getStartDate());
      subscription.setStartDate(request.getStartDate());
      subscription.setNextDeliveryDate(
          serviceHelper.calculateNextDeliveryDate(
              subscription.getSubscriptionPlan().getFrequency(), subscription));
      changedFound = true;
    }
    if (request.getEndDate() != null && !request.getEndDate().equals(subscription.getEndDate())) {
      log.info(
          "Change found in end date from {} ==> {} ",
          subscription.getStartDate(),
          request.getStartDate());
      subscription.setEndDate(request.getEndDate());
      changedFound = true;
    }
    SubscriptionResponse response =
        new SubscriptionResponse(true, "No subscription changes found!!");
    if (changedFound) {
      subscription.setUpdateVersion(subscription.getUpdateVersion() + 1);
      repoManager.getSubscriptionRepo().save(subscription);
      response = new SubscriptionResponse(true, "Subscription updated successfully");
    }
    log.info("End - Update subscription request for customer {}", userId);
    return response;
  }

  @Transactional
  public SubscriptionResponse updateSubscriptionStatus(
      Long userId, Long subId, SubscriptionStatus status) {
    log.info("Start - Update subscription_status request for customer {}", userId);
    var subscription =
        repoManager
            .getSubscriptionRepo()
            .findById(subId)
            .orElseThrow(
                () ->
                    new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Subscription not found"));
    SubscriptionResponse response =
        new SubscriptionResponse(true, "No subscription status changes found!!");
    boolean changeEligible = false;
    switch (subscription.getStatus()) {
      case PENDING, ACTIVE -> {
        // It can be changed to PAUSED
        if (!status.equals(SubscriptionStatus.PENDING)
            && !status.equals(SubscriptionStatus.ACTIVE)) {
          changeEligible = true;
        }
      }
      case PAUSED -> {
        if (!status.equals(SubscriptionStatus.PAUSED)) {
          changeEligible = true;
        }
        // It can be changed to ACTIVE
      }
      case CANCELLED -> {
        throw new APIException(
            APIErrorCode.BAD_REQUEST_RECEIVED, "Subscription already cancelled.");
      }
      case EXPIRED -> {
        throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Subscription already expired.");
      }
      case null, default -> {
        throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid subscription status.");
      }
    }
    if (changeEligible) {
      log.info("Change found in status from {} ==>> {} ", subscription.getStatus(), status);
      subscription.setStatus(status);
      subscription.setUpdateVersion(subscription.getUpdateVersion() + 1);
      repoManager.getSubscriptionRepo().save(subscription);
      response = new SubscriptionResponse(true, "Subscription status updated successfully");
    }
    log.info("End - Update subscription_status request for customer {}", userId);
    return response;
  }

  @Transactional
  public void deleteSubscription(Long subscriptionId) {
    repoManager.getSubscriptionRepo().deleteById(subscriptionId);
  }

  @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
  public Subscription fetchSubscription(final Long subId) {
    return repoManager
        .getSubscriptionRepo()
        .findById(subId)
        .orElseThrow(
            () ->
                new APIException(
                    APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid subscription id " + subId));
  }

  /**
   * @param vendorId
   * @return
   */
  @Transactional(readOnly = true)
  public List<SubscriptionDetailDTO> fetchSubsByVendor(final Long vendorId) {
    log.debug("Start 'fetchSubscriptionsByVendorId' for vendor:{}", vendorId);
    var subs = repoManager.getSubscriptionRepo().findByVendorId(vendorId);
    if (subs == null || subs.isEmpty()) {
      return Collections.emptyList();
    }
    return subs.stream().map(this::convertToDTO).toList();
  }

  /**
   * @param vendorId
   * @return
   */
  @Transactional(readOnly = true)
  public List<SubscriptionDetailDTO> fetchSubsByUserAndVendor(
      final Long userId, final Long vendorId) {
    log.debug("Start 'fetchByCustomerIdAndVendorId' for user :{} and vendor:{}", userId, vendorId);
    var subs = repoManager.getSubscriptionRepo().findByUserIdAndVendorId(userId, vendorId);
    log.debug("No of subscription  {} for user :{} and vendor {}", subs.size(), userId, vendorId);
    if (subs.isEmpty()) {
      return Collections.emptyList();
    }
    return subs.stream().map(this::convertToDTO).toList();
  }

  @Transactional(readOnly = true)
  public Optional<Subscription> fetchByUserIdAndSkuId(final Long userId, final Long skuId) {
    return repoManager.getSubscriptionRepo().findByUserIdAndSkuId(userId, skuId);
  }

  @Transactional(readOnly = true)
  public Object fetchSubsByUserId(Long userId) {
    var subs = repoManager.getSubscriptionRepo().findByUserId(userId);
    log.debug("No of subscription  {} for user :{}", subs.size(), userId);
    if (subs.isEmpty()) {
      return Collections.emptyList();
    }
    return subs.stream().map(this::convertToDTO).toList();
  }

  private SubscriptionDetailDTO convertToDTO(Object[] record) {
    return new SubscriptionDetailDTO(
        (Long) record[0], // subscriptionId
        (Long) record[1], // userId
        (Long) record[2], // vendorId
        (String) record[3], // vendorName
        (BigDecimal) record[4], // listPrice
        (BigDecimal) record[5], // salePrice
        (Integer) record[6], // quantity
        (BigDecimal) record[7], // amount
        (String) record[8], // skuName
        convertToLocalDate(record[9]), // startDate
        (String) record[10], // frequency
        (String) record[11], // status
        convertToLocalDate(record[12]), // nextDeliveryDate
        (String) record[13], // sku name
        (String) record[14] // image_path
        );
  }

  private LocalDate convertToLocalDate(Object sqlDate) {
    return sqlDate != null ? ((java.sql.Date) sqlDate).toLocalDate() : null;
  }
}
