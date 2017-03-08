package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.ConflictException;

/** Created by AngeloChen on 08/03/2017. */
public class SellCouponGrantInsufficientException extends ConflictException {

  /** String error code for the exception. */
  public static final String ERROR_CODE =
      ExceptionCodeBase.CONFLICT_ERROR_BASE + "sell_coupon_grant_remain";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_CONFLICT_ERROR_BASE + 5;

  /**
   * Constructor.
   *
   * @param sellCouponGrantId sell coupon grant id.
   */
  public SellCouponGrantInsufficientException(String sellCouponGrantId) {
    super(
        NUMERIC_ERROR_CODE, ERROR_CODE, "Sell coupon grant remain insufficient", sellCouponGrantId);
  }
}
