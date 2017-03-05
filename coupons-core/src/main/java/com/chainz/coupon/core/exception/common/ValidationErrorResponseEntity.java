package com.chainz.coupon.core.exception.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Validation error message.
 */
@Getter
@Setter
public class ValidationErrorResponseEntity {

  private List<ValidationFieldErrorEntity> errors = new ArrayList<>();

  /**
   * Initialize from validation error response.
   *
   * @param objectErrors collection of validation object error.
   */
  public ValidationErrorResponseEntity fromObjectErrors(Collection<ObjectError> objectErrors) {
    objectErrors.forEach(objectError -> {
      ValidationFieldErrorEntity entity = new ValidationFieldErrorEntity();
      entity.setCode(objectError.getCode());
      entity.setDefaultMessage(objectError.getDefaultMessage());
      if (objectError instanceof FieldError) {
        entity.setField(((FieldError) objectError).getField());
      }
      errors.add(entity);
    });
    return this;
  }

  @Data
  public static class ValidationFieldErrorEntity {
    private String field;

    private String defaultMessage;

    private String code;
  }

}
