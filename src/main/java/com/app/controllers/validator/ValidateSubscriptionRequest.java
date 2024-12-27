package com.app.controllers.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SubscriptionRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateSubscriptionRequest {

  String message() default "Invalid subscription request";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
