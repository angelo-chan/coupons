package com.chainz.coupon.core.credentials;

/** Operator manager. */
public class OperatorManager {

  private static final ThreadLocal<Operator> OPERATOR = new ThreadLocal<>();

  /**
   * Set operator.
   *
   * @param operator operator
   */
  public static void setOperator(Operator operator) {
    OPERATOR.set(operator);
  }

  /**
   * Get operator.
   *
   * @return operator.
   */
  public static Operator getOperator() {
    return OPERATOR.get();
  }

  /** clean operator. */
  public static void cleanOperator() {
    OPERATOR.remove();
  }
}
