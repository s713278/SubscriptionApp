package com.app.controllers.validator;

import com.app.config.AppConstants;
import com.app.entites.type.SubFrequency;
import com.app.payloads.request.CreateSubscriptionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class SubscriptionRequestValidator
    implements ConstraintValidator<ValidateSubscriptionRequest, CreateSubscriptionRequest> {

  @Override
  public boolean isValid(CreateSubscriptionRequest value, ConstraintValidatorContext context) {
    if (value == null) {
      return true; // You can handle null cases separately if needed
    }

    // If frequency is ONE_TIME, check deliveryDate
    if (SubFrequency.ONE_TIME /*value.getFrequency()*/ == SubFrequency.ONE_TIME) {
      if (value.getDeliveryDate() == null) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(
                "Delivery date is required for ONE_TIME subscription")
            // .addPropertyNode("deliveryDate")
            .addConstraintViolation();
        return false;
      }
      return validateStartDate(value.getDeliveryDate(), context);
    } else {
      // For other frequencies, check startDate and endDate
      if (value.getStartDate() == null) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate(
                "Start Date is required for recurring subscriptions")
            // .addPropertyNode("startDate")
            .addConstraintViolation();
        return false;
      }
      return validateEndDate(value.getStartDate(), value.getEndDate(), context);
    }
  }

  /**
   * Start date max allowed date validation
   *
   * @param startDate
   * @param context
   * @return
   */
  private boolean validateStartDate(LocalDate startDate, ConstraintValidatorContext context) {
    LocalDate today = LocalDate.now();
    LocalDate maxAllowedDate = today.plusDays(AppConstants.SUB_MAX_ALLOWED_START_DATE_DAYS);
    if (startDate == null) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate("Start date is required.")
          .addPropertyNode("start_date")
          .addConstraintViolation();
      return false;
    }
    if (startDate.isBefore(today) || startDate.isAfter(maxAllowedDate)) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              String.format("Start date must be between today (%s) and %s.", today, maxAllowedDate))
          .addPropertyNode("start_date")
          .addConstraintViolation();
      return false;
    }
    return true;
  }

  /**
   * End date max allowed date validation
   *
   * @param startDate
   * @param endDate
   * @param context
   * @return
   */
  private boolean validateEndDate(
      LocalDate startDate, LocalDate endDate, ConstraintValidatorContext context) {
    LocalDate today = LocalDate.now();
    LocalDate maxAllowedDate = today.plusDays(AppConstants.SUB_MAX_ALLOWED_END_DATE_DAYS);
    if (endDate.isBefore(startDate) || endDate.isAfter(maxAllowedDate)) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              String.format(
                  "End date must be between today (%s) and %s.", startDate, maxAllowedDate))
          .addPropertyNode("end_date")
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}
