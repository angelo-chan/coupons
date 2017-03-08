package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.ConflictException;

/** Sell coupon insufficient. */
public class SellCouponInsufficientException extends ConflictException {
  /** String error code for the exception. */
  public static final String ERROR_CODE = ExceptionCodeBase.CONFLICT_ERROR_BASE + "sell_coupon_sku";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_CONFLICT_ERROR_BASE + 3;

  /**
   * Constructor.
   *
   * @param sellCouponId sell coupon id.
   */
  public SellCouponInsufficientException(Long sellCouponId) {
    super(NUMERIC_ERROR_CODE, ERROR_CODE, "Sell coupon sku insufficient", sellCouponId);
  }
}
