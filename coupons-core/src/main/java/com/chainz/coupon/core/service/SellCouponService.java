package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponInsufficientException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;

/** Sell coupon interface. */
public interface SellCouponService {

  /**
   * Grant coupon to seller.
   *
   * @param grantCode grant code.
   * @throws InvalidGrantCodeException invalid grant code.
   * @throws CouponStatusConflictException coupon status conflict.
   * @throws CouponInsufficientException  coupon insufficient.
   */
  void grant(String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException, CouponInsufficientException;
}
