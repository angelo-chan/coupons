package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.NotFoundException;

/** Sell coupon grant not found. */
public class SellCouponGrantNotFoundException extends NotFoundException {

  /** String error code for the exception. */
  public static final String ERROR_CODE =
      ExceptionCodeBase.NOT_FOUND_ERROR_BASE + "sell_coupon_grant";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_NOT_FOUND_ERROR_BASE + 5;

  /**
   * Constructor.
   *
   * @param grantCode grant code.
   */
  public SellCouponGrantNotFoundException(String grantCode) {
    super(
        NUMERIC_ERROR_CODE,
        ERROR_CODE,
        "There is no sell coupon grant for grant code [{}]",
        grantCode);
  }
}
