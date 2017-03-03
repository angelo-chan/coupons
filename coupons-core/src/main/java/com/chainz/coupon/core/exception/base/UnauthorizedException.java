package com.chainz.coupon.core.exception.base;

/**
 * Base exception for unauthorized.
 */
public class UnauthorizedException extends ApplicationException {

  public static final String DEFAULT_ERROR_CODE = "errors.com.chainz.unauthorized";

  public static final int HTTP_STATUS_CODE = 401;

  public UnauthorizedException(int numericErrorCode, String errorCode, String pattern, Object... args) {
    super(HTTP_STATUS_CODE, numericErrorCode, errorCode != null ? errorCode : DEFAULT_ERROR_CODE, pattern, args);
  }

}
