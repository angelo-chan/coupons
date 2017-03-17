package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.ConflictException;

/** Coupon expired exception. */
public class CouponExpiredException extends ConflictException {
  /** String error code for the exception. */
  public static final String ERROR_CODE = ExceptionCodeBase.CONFLICT_ERROR_BASE + "coupon_expired";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_CONFLICT_ERROR_BASE + 7;

  /**
   * Constructor.
   *
   * @param couponId coupon id.
   */
  public CouponExpiredException(Long couponId) {
    super(NUMERIC_ERROR_CODE, ERROR_CODE, "Coupon [{}] already expired", couponId);
  }
}
