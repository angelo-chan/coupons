package com.chainz.coupon.core.exception.base;

/** Base exception for forbidden. */
public class ForbiddenException extends ApplicationException {

  public static final int HTTP_STATUS_CODE = 403;

  public static final String DEFAULT_ERROR_CODE = "errors.com.chianz.forbidden";

  /**
   * constructor.
   *
   * @param numericErrorCode numeric error code.
   * @param errorCode error code.
   * @param pattern message pattern.
   * @param args args.
   */
  public ForbiddenException(
      int numericErrorCode, String errorCode, String pattern, Object... args) {
    super(
        HTTP_STATUS_CODE,
        numericErrorCode,
        errorCode != null ? errorCode : DEFAULT_ERROR_CODE,
        pattern,
        args);
  }
}
