package com.chainz.coupon.core.exception.base;

/**
 * Base exception for bad request.
 */
public class BadRequestException extends ApplicationException {

  public static final int HTTP_STATUS_CODE = 400;

  public static final String DEFAULT_ERROR_CODE = "errors.com.chianz.bad_request";

  public BadRequestException(int numericErrorCode, String errorCode, String pattern, Object... args) {
    super(HTTP_STATUS_CODE, numericErrorCode, errorCode != null ? errorCode : DEFAULT_ERROR_CODE, pattern, args);
  }

}
