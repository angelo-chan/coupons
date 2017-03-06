package com.chainz.coupon.core.exception;

import com.chainz.coupon.core.exception.base.UnauthorizedException;

/** Operator invalid exception. */
public class UnAuthorizedOperatorException extends UnauthorizedException {

  public UnAuthorizedOperatorException() {
    super(-1, null, "Unauthorized operator.");
  }
}
