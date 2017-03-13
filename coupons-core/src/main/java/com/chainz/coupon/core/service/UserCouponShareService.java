package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.UserCouponShareNotFoundException;
import com.chainz.coupon.shared.objects.UserCouponShareInfo;

/** user coupon share interface provide functionality to work with user coupon share. */
public interface UserCouponShareService {

  /**
   * Get user coupon share.
   *
   * @param shareCode share code.
   * @return user coupon share info.
   * @throws UserCouponShareNotFoundException user coupon share not found.
   */
  UserCouponShareInfo getUserCouponShare(String shareCode) throws UserCouponShareNotFoundException;

  /**
   * abort user coupon share.
   *
   * @param shareCode grant code.
   */
  void abortUserCouponShare(String shareCode) throws UserCouponShareNotFoundException;
}
