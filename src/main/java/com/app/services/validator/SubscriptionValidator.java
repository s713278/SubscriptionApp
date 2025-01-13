package com.app.services.validator;

import com.app.config.AppConstants;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import java.time.LocalDate;
import java.util.List;

public class SubscriptionValidator {
  /**
   * @param date
   */
  public static void validateDeliveryDate(LocalDate date) {
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

  /**
   * @param date
   */
  public static void validateStartDate(LocalDate date) {
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

  public static void validateCustomDays(List<Integer> customDays) {
    if (customDays == null || customDays.isEmpty()) {
      throw new APIException(
          APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED, "Custom days are required.");
    }
  }
}
