package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.NotFoundException;

/**
 * User coupon not found exception.
 */
public class UserCouponNotFoundException extends NotFoundException{

  /** String error code for the exception. */
  public static final String ERROR_CODE = ExceptionCodeBase.NOT_FOUND_ERROR_BASE + "user_coupon";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_NOT_FOUND_ERROR_BASE + 4;

  /**
   * Constructor.
   * @param userCouponId  user coupon id.
   */
  public UserCouponNotFoundException(Long userCouponId) {
    super(NUMERIC_ERROR_CODE, ERROR_CODE, "There is no user coupon [{}]", userCouponId);
  }
}
