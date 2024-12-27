package com.app.controllers.validator;

import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public abstract class AbstractRequestValidation {

  protected void validateRequest(final BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      // Create a map to store error messages
      // Map<String, String> errors = new HashMap<>();
      List<String> errors = new ArrayList<>();
      for (FieldError error : bindingResult.getFieldErrors()) {
        errors.add(error.getField() + ":" + error.getDefaultMessage());
      }
      bindingResult
          .getAllErrors()
          .forEach(objectError -> errors.add(objectError.getDefaultMessage()));
      // Return the error map as the response with BAD_REQUEST status
      throw new APIException(APIErrorCode.API_400, errors.toString());
    }
  }
}
