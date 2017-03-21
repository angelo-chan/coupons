package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponGrantNotFoundException;
import com.chainz.coupon.shared.objects.CouponGrantInfo;

/** Coupon grant interface provide functionality to work with coupon grant. */
public interface CouponGrantService {
  /**
   * Get sell coupon grant.
   *
   * @param grantCode grant code.
   * @return  coupon grant info.
   * @throws CouponGrantNotFoundException sell coupon grant not found.
   */
  CouponGrantInfo getCouponGrant(String grantCode) throws CouponGrantNotFoundException;

}
