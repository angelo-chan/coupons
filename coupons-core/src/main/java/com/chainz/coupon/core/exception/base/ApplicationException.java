package com.chainz.coupon.core.exception.base;

import lombok.Getter;
import lombok.Setter;

/** Application exception, the general exception for chainz. */
@Getter
@Setter
public abstract class ApplicationException extends BaseException {

  public static final String DEFAULT_ERROR_CODE = "errors.com.chainz.unknown";

  public static final int DEFAULT_NUMERIC_ERROR_CODE = -1;

  protected int numericErrorCode = DEFAULT_NUMERIC_ERROR_CODE;

  protected String errorCode = DEFAULT_ERROR_CODE;

  protected int httpStatusCode = 500;

  protected ApplicationException(Exception e) {
    super(e);
  }

  protected ApplicationException(
      int httpStatusCode, int numericErrorCode, String errorCode, String pattern, Object... args) {
    super(pattern, args);
    this.httpStatusCode = httpStatusCode;
    this.numericErrorCode = numericErrorCode;
    this.errorCode = errorCode != null ? errorCode : DEFAULT_ERROR_CODE;
  }
}
