package com.app.controllers.validator;

import java.time.LocalDate;

import com.app.config.AppConstants;
import com.app.entites.type.SubFrequency;
import com.app.payloads.request.SubscriptionRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class SubscriptionRequestValidator implements ConstraintValidator<ValidateSubscriptionRequest, SubscriptionRequest> {

    @Override
    public boolean isValid(SubscriptionRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // You can handle null cases separately if needed
        }

        // If frequency is ONE_TIME, check deliveryDate
        if (value.getFrequency() == SubFrequency.ONE_TIME) {
            if (value.getDeliveryDate() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Delivery date is required for ONE_TIME frequency")
                       //.addPropertyNode("deliveryDate")
                       .addConstraintViolation();
                return false;
            }else{
                return validateStartDate(value.getDeliveryDate(),context);
            }
        } else {
            // For other frequencies, check startDate and endDate
            if (value.getStartDate() == null || value.getEndDate() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Start Date and End Date are required for this frequency")
                       //.addPropertyNode("startDate")
                       .addConstraintViolation();
                return false;
            }else{
                return validateEndDate(value.getStartDate(),value.getEndDate(),context);
            }
        }

    }

    /**
     * <p>Start date max allowed date validation</p>
     * @param startDate
     * @param context
     * @return
     */
    private boolean validateStartDate(LocalDate startDate,ConstraintValidatorContext context) {
        LocalDate today = LocalDate.now();
        LocalDate maxAllowedDate = today.plusDays(AppConstants.SUB_MAX_ALLOWED_START_DATE_DAYS);

        if (startDate.isBefore(today) || startDate.isAfter(maxAllowedDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate( String.format("Start date must be between today (%s) and %s.", today, maxAllowedDate))
                    .addPropertyNode("start_date")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    /**
     * <p>End date max allowed date validation</p>
     * @param startDate
     * @param endDate
     * @param context
     * @return
     */
    private boolean validateEndDate(LocalDate startDate,LocalDate endDate,ConstraintValidatorContext context) {
        LocalDate today = LocalDate.now();
        LocalDate maxAllowedDate = today.plusDays(AppConstants.SUB_MAX_ALLOWED_END_DATE_DAYS);

        if (endDate.isBefore(startDate) || endDate.isAfter(maxAllowedDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate( String.format("End date must be between today (%s) and %s.", startDate, maxAllowedDate))
                    .addPropertyNode("end_date")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}