package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.ConflictException;
import com.chainz.coupon.shared.objects.SellCouponGrantStatus;

/** Sell coupon grant status conflict exception. */
public class SellCouponGrantStatusConflictException extends ConflictException {

  /** String error code for the exception. */
  public static final String ERROR_CODE =
      ExceptionCodeBase.CONFLICT_ERROR_BASE + "sell_coupon_grant_status";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_CONFLICT_ERROR_BASE + 4;

  /**
   * Constructor.
   *
   * @param sellCouponGrantId sell coupon grant id.
   * @param status sell coupon grant status.
   */
  public SellCouponGrantStatusConflictException(
      String sellCouponGrantId, SellCouponGrantStatus status) {
    super(
        NUMERIC_ERROR_CODE,
        ERROR_CODE,
        "Sell coupon grant [{}] in status [{}]",
        sellCouponGrantId,
        status);
  }
}
