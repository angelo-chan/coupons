package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.ConflictException;
import com.chainz.coupon.shared.objects.UserCouponShareStatus;

/** User coupon share status conflict exception. */
public class UserCouponShareStatusConflictException extends ConflictException {

  /** String error code for the exception. */
  public static final String ERROR_CODE =
      ExceptionCodeBase.CONFLICT_ERROR_BASE + "user_coupon_share_status";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_CONFLICT_ERROR_BASE + 5;

  /**
   * Constructor.
   *
   * @param userCouponShareId user coupon share id.
   * @param status user coupon share status.
   */
  public UserCouponShareStatusConflictException(
      String userCouponShareId, UserCouponShareStatus status) {
    super(
        NUMERIC_ERROR_CODE,
        ERROR_CODE,
        "UserCouponShare [{}] in status [{}]",
        userCouponShareId,
        status);
  }
}
