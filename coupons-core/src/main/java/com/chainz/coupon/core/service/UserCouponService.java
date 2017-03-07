package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponInsufficientException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;

/** user coupon interface provide functionality to work with user coupon. */
public interface UserCouponService {
  /**
   * Grant sell coupon to specific user.
   *
   * @param grantCode grant code.
   * @throws InvalidGrantCodeException invalid grant code.
   * @throws CouponStatusConflictException coupon status conflict.
   * @throws CouponInsufficientException coupon insufficient.
   */
  void granted(String grantCode)
    throws InvalidGrantCodeException, CouponStatusConflictException, CouponInsufficientException;

}
