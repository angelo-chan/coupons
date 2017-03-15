package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.ConflictException;

/** User coupon get limit exception. */
public class CouponGetLimitException extends ConflictException {
  /** String error code for the exception. */
  public static final String ERROR_CODE =
      ExceptionCodeBase.CONFLICT_ERROR_BASE + "coupon_get_limit";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_CONFLICT_ERROR_BASE + 6;

  /**
   * Constructor.
   *
   * @param couponId coupon id.
   * @param getLimit get limit.
   */
  public CouponGetLimitException(Long couponId, long getLimit) {
    super(NUMERIC_ERROR_CODE, ERROR_CODE, "Coupon [{}] get limit [{}]", couponId, getLimit);
  }
}
