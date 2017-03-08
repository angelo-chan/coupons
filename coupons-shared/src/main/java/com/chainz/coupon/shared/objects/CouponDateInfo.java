package com.chainz.coupon.shared.objects;

import com.chainz.coupon.shared.validator.CouponDateInfoValidator;
import com.chainz.coupon.shared.validator.common.EnumerationValidator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/** Coupon date info. */
@Getter
@Setter
@CouponDateInfoValidator
public class CouponDateInfo {

  @NotNull
  @EnumerationValidator(CouponDateType.class)
  private String dateType;

  @Min(0)
  private Integer fixedBeginTerm;

  @Min(1)
  private Integer fixedTerm;

  private LocalDate timeRangeStart;

  private LocalDate timeRangeEnd;
}
