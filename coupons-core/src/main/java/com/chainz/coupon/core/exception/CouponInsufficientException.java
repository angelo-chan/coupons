package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.ConflictException;

/** Coupon insufficient exception. */
public class CouponInsufficientException extends ConflictException {
  /** String error code for the exception. */
  public static final String ERROR_CODE = ExceptionCodeBase.CONFLICT_ERROR_BASE + "coupon";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_CONFLICT_ERROR_BASE + 2;

  public CouponInsufficientException(Long couponId) {
    super(NUMERIC_ERROR_CODE, ERROR_CODE, "Coupon sku insufficient", couponId);
  }
}
