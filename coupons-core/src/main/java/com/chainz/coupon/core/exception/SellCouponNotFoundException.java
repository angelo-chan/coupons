package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.NotFoundException;

/** Sell coupon not found exception. */
public class SellCouponNotFoundException extends NotFoundException {
  /** String error code for the exception. */
  public static final String ERROR_CODE = ExceptionCodeBase.NOT_FOUND_ERROR_BASE + "sell_coupon";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_NOT_FOUND_ERROR_BASE + 2;

  public SellCouponNotFoundException(Long sellCouponId) {
    super(NUMERIC_ERROR_CODE, ERROR_CODE, "There is no sell coupon [{}]", sellCouponId);
  }
}
