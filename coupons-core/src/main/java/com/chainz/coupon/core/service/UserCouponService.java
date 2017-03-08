package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.SellCouponGrantInsufficientException;
import com.chainz.coupon.core.exception.SellCouponGrantStatusConflictException;

/** user coupon interface provide functionality to work with user coupon. */
public interface UserCouponService {
  /**
   * Grant sell coupon to specific user.
   *
   * @param grantCode grant code.
   * @throws InvalidGrantCodeException invalid grant code.
   * @throws CouponStatusConflictException coupon status conflict.
   * @throws SellCouponGrantInsufficientException sell coupon insufficient.
   * @throws SellCouponGrantStatusConflictException sell coupon grant status conflict.
   */
  void granted(String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException,
          SellCouponGrantInsufficientException, SellCouponGrantStatusConflictException;
}
