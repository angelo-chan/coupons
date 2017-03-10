package com.chainz.coupon.core.credentials;

import com.chainz.coupon.core.exception.UnAuthorizedOperatorException;
import com.chainz.coupon.core.utils.Constants;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/** Permission aspect. */
@Aspect
@Component
public class PermissionAspect {

  /** Check client permission. */
  @Before("@annotation(ClientPermission)")
  public void checkClientPermission() {
    Operator operator = OperatorManager.getOperator();
    if (operator == null || !operator.isClient()) {
      throw new UnAuthorizedOperatorException();
    }
  }
}
