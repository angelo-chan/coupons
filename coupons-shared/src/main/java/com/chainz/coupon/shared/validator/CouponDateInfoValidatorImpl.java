package com.chainz.coupon.shared.validator;

import com.chainz.coupon.shared.objects.CouponDateInfo;
import com.chainz.coupon.shared.objects.CouponDateType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/** CouponDateInfoValidator implementation. */
public class CouponDateInfoValidatorImpl
    implements ConstraintValidator<CouponDateInfoValidator, CouponDateInfo> {

  @Override
  public void initialize(CouponDateInfoValidator couponDateInfoValidator) {}

  @Override
  public boolean isValid(
      CouponDateInfo couponDateInfo, ConstraintValidatorContext constraintValidatorContext) {
    if (CouponDateType.DATE_TYPE_FIXED_TERM.toString().equals(couponDateInfo.getDateType())) {
      return couponDateInfo.getFixedBeginTerm() != null && couponDateInfo.getFixedTerm() != null;
    } else {
      LocalDate start = couponDateInfo.getTimeRangeStart();
      LocalDate end = couponDateInfo.getTimeRangeEnd();
      return start != null && end != null && !end.isBefore(start);
    }
  }
}
