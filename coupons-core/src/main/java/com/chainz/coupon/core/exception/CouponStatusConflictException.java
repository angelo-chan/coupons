package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.ConflictException;
import com.chainz.coupon.shared.objects.CouponStatus;

/** Coupon status conflict exception. */
public class CouponStatusConflictException extends ConflictException {
  /** String error code for the exception. */
  public static final String ERROR_CODE = ExceptionCodeBase.CONFLICT_ERROR_BASE + "coupon";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_CONFLICT_ERROR_BASE + 1;

  public CouponStatusConflictException(Long couponId, CouponStatus status) {
    super(NUMERIC_ERROR_CODE, ERROR_CODE, "Coupon [{}] in status [{}]", couponId, status);
  }
}
