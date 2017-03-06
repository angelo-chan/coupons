package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.NotFoundException;

/** Invalid grant code. */
public class InvalidGrantCodeException extends NotFoundException {
  /** String error code for the exception. */
  public static final String ERROR_CODE = ExceptionCodeBase.NOT_FOUND_ERROR_BASE + "grant_code";

  /** Numeric error code for the exception. */
  public static final int NUMERIC_ERROR_CODE = ExceptionCodeBase.NUMERIC_NOT_FOUND_ERROR_BASE + 2;

  public InvalidGrantCodeException() {
    super(NUMERIC_ERROR_CODE, ERROR_CODE, "Invalid coupon grant code");
  }
}
