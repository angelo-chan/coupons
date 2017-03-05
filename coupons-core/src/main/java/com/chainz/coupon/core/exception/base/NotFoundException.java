package com.chainz.coupon.core.exception.base;

/** Base exception for not found. */
public class NotFoundException extends ApplicationException {

  public static final int HTTP_STATUS_CODE = 404;

  public static final String DEFAULT_ERROR_CODE = "errors.com.chianz.not_found";

  /**
   * constructor.
   *
   * @param numericErrorCode numeric error code.
   * @param errorCode error code.
   * @param pattern message pattern.
   * @param args args.
   */
  public NotFoundException(int numericErrorCode, String errorCode, String pattern, Object... args) {
    super(
        HTTP_STATUS_CODE,
        numericErrorCode,
        errorCode != null ? errorCode : DEFAULT_ERROR_CODE,
        pattern,
        args);
  }
}
