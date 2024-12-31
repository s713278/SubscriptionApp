package com.app.controllers.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SkuCreateRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateSkuCreateRequest {

  String message() default "Invalid create SKU request";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
