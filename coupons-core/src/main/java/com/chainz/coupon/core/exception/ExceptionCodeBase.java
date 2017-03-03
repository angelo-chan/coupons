package com.chainz.coupon.core.exception;

/**
 * Exception code base .
 */
public interface ExceptionCodeBase {


  /**
   * Numeric error codes range base for coupon service specific exceptions.
   */
  int NUMERIC_ERROR_CODE_RANGE_BASE = 10000;

  /**
   * Numeric error codes range base for "bad request" family exceptions.
   */
  int NUMERIC_BAD_REQUEST_RANGE_BASE = NUMERIC_ERROR_CODE_RANGE_BASE;

  /**
   * Numeric error codes range base for "not found" family exceptions.
   */
  int NUMERIC_NOT_FOUND_ERROR_BASE = NUMERIC_ERROR_CODE_RANGE_BASE + 400;

  /**
   * Numeric error codes range base for "conflict" family exceptions.
   */
  int NUMERIC_CONFLICT_ERROR_BASE = NUMERIC_ERROR_CODE_RANGE_BASE + 900;

  /**
   * String error codes base for team service specific exceptions.
   */
  String ERROR_CODE_BASE = "errors.com.chainz.coupon.";

  /**
   * String error codes base for "bad request" family exceptions.
   */
  String BAD_REQUEST_ERROR_BASE = ERROR_CODE_BASE + "bad_request.";

  /**
   * String error codes base for "not found" family exceptions.
   */
  String NOT_FOUND_ERROR_BASE = ERROR_CODE_BASE + "not_found.";

  /**
   * String error codes base for "conflict" family exceptions.
   */
  String CONFLICT_ERROR_BASE = ERROR_CODE_BASE + "conflict.";

}
