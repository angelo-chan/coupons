package com.chainz.coupon.core.utils;

/** Constants. */
public interface Constants {

  String SYSTEM = "SYSTEM";
  String VENDOR = "VENDOR";
  String STORE = "STORE";
  String CLIENT = "CLIENT";

  String COUPON_GRANT_PREFIX = "COUPON_GRANT:";
  int COUPON_GRANT_TIMEOUT = 24 * 3600;
  String SELL_COUPON_GRANT_PREFIX = "SELL_COUPON_GRANT:";
  int SELL_COUPON_GRANT_TIMEOUT = 24 * 3600;
}
