package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.NotFoundException;

/** Invalid share code exception. */
public class InvalidShareCodeException extends NotFoundException {
  /** String error code for the exception. */
  public static final String ERROR_CODE = ExceptionCodeBase.NOT_FOUND_ERROR_BASE + "share_code";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_NOT_FOUND_ERROR_BASE + 7;

  /**
   * Constructor.
   *
   * @param shareCode grant code.
   */
  public InvalidShareCodeException(String shareCode) {
    super(NUMERIC_ERROR_CODE, ERROR_CODE, "Invalid user coupon share code [{}]", shareCode);
  }
}
