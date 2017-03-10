package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.NotFoundException;

/** User coupon share not found exception. */
public class UserCouponShareNotFoundException extends NotFoundException {
  /** String error code for the exception. */
  public static final String ERROR_CODE =
      ExceptionCodeBase.NOT_FOUND_ERROR_BASE + "user_coupon_share";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_NOT_FOUND_ERROR_BASE + 6;

  /**
   * Constructor.
   *
   * @param shareCode share code.
   */
  public UserCouponShareNotFoundException(String shareCode) {
    super(
        NUMERIC_ERROR_CODE,
        ERROR_CODE,
        "There is no user coupon share for share code [{}]",
        shareCode);
  }
}
