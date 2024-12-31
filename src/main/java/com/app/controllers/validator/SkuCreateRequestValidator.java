package com.app.controllers.validator;

import com.app.entites.type.DeliveryMode;
import com.app.entites.type.SkuType;
import com.app.entites.type.SubFrequency;
import com.app.payloads.ServiceAttributeDTO;
import com.app.payloads.request.SkuCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SkuCreateRequestValidator
    implements ConstraintValidator<ValidateSkuCreateRequest, SkuCreateRequest> {
  @Override
  public boolean isValid(SkuCreateRequest request, ConstraintValidatorContext context) {
    if (request == null) {
      return false; // You can handle null cases separately if needed
    }
    if (request.getSkuType() == SkuType.SERVICE) {
      // Nullify Eligible Plans
      request.setEligibleSubPlans(null);
      request.setSubscriptionEligible(false);
      context.disableDefaultConstraintViolation();
      if (request.getServiceAttributes() == null) {
        context
            .buildConstraintViolationWithTemplate(
                "ServiceAttribute must be provided for SKU with type SERVICE")
            .addConstraintViolation();
        return false;
      }

      // Validate fields in ServiceAttribute
      ServiceAttributeDTO serviceAttributes = request.getServiceAttributes();
      if (serviceAttributes.getValidDays() == null || serviceAttributes.getValidDays() <= 0) {
        context
            .buildConstraintViolationWithTemplate(
                "ServiceAttribute.validDays must be a positive number.")
            .addConstraintViolation();
        return false;
      }

      if (serviceAttributes.getNoOfUses() == null || serviceAttributes.getNoOfUses() <= 0) {
        context
            .buildConstraintViolationWithTemplate(
                "ServiceAttribute.noOfUses must be a positive number.")
            .addConstraintViolation();
        return false;
      }

    } else if (request.getSkuType() == SkuType.ITEM) {
      request.setServiceAttributes(null);
      // Validate recurring eligibility
      if (request.isSubscriptionEligible()) {
        if (request.getEligibleSubPlans() == null || request.getEligibleSubPlans().isEmpty()) {
          context
              .buildConstraintViolationWithTemplate(
                  "SKU marked as subscription eligible must have at least one subscription plan")
              .addConstraintViolation();
          return false;
        }

        if (request.getEligibleSubPlans().size() > 1) {
          boolean hasInvalidCombination =
              request.getEligibleSubPlans().stream()
                  .anyMatch(
                      plan ->
                          plan.getFrequency() == SubFrequency.ONE_TIME
                              && plan.getDeliveryMode() == DeliveryMode.FIXED);
          if (hasInvalidCombination) {
            context
                .buildConstraintViolationWithTemplate(
                    "ONE_TIME SKU with FIXED delivery mode is not valid for recurring plans.")
                .addConstraintViolation();
            return false;
          }
        }
      } else {
        // Verify the Delivery Mode
        if (request.getEligibleSubPlans().size() > 1) {
          context
              .buildConstraintViolationWithTemplate(
                  "SKU is not eligible for subscription,So Its not eligible for having subscription plans.")
              .addConstraintViolation();
          return false;
        }
        boolean hasInvalidDeliveryModel =
            request.getEligibleSubPlans().stream()
                .anyMatch(
                    plan ->
                        plan.getFrequency() != SubFrequency.ONE_TIME
                            && plan.getDeliveryMode() != DeliveryMode.FIXED);
        if (hasInvalidDeliveryModel) {
          context
              .buildConstraintViolationWithTemplate(
                  "Other than ONE_TIME SKU with FIXED delivery mode is not valid for non-recurring plans.")
              .addConstraintViolation();
          return false;
        }
      }
    }
    return true;
  }
}
