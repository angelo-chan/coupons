package com.chainz.coupon.core.exception.base;

/**
 * Base exception for service unavailable.
 */
public class ServiceUnavailableException extends ApplicationException {

  public static final String DEFAULT_ERROR_CODE = "errors.com.chainz.service_unavailable";

  public static final int HTTP_STATUS_CODE = 500;

  public ServiceUnavailableException(int numericErrorCode, String errorCode, String pattern, Object... args) {
    super(HTTP_STATUS_CODE, numericErrorCode, errorCode != null ? errorCode : DEFAULT_ERROR_CODE, pattern, args);
  }

}
