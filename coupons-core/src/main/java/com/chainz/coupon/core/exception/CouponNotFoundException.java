package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.NotFoundException;

/**
 * Coupon not exist exception.
 */
public class CouponNotFoundException extends NotFoundException {

  /**
   * String error code for the exception.
   */
  public static final String ERROR_CODE = ExceptionCodeBase.NOT_FOUND_ERROR_BASE + "coupon";

  /**
   * Numeric error code for the exception.
   */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_NOT_FOUND_ERROR_BASE + 1;

  public CouponNotFoundException(Long couponId) {
    super(NUMERIC_ERROR_CODE, ERROR_CODE, "There is no coupon [{}]", couponId);
  }
}
