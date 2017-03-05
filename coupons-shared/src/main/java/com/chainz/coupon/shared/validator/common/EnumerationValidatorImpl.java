package com.chainz.coupon.shared.validator.common;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/** EnumerationValidator implementation. */
public class EnumerationValidatorImpl implements ConstraintValidator<EnumerationValidator, String> {

  List<String> valueList = null;

  @Override
  public void initialize(EnumerationValidator enumerationValidator) {
    valueList = new ArrayList<>();
    Class<? extends Enum<?>> enumClass = enumerationValidator.value();

    Enum[] enumValArr = enumClass.getEnumConstants();

    for (Enum enumVal : enumValArr) {
      valueList.add(enumVal.toString());
    }
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (!valueList.contains(value)) {
      return false;
    }
    return true;
  }
}
