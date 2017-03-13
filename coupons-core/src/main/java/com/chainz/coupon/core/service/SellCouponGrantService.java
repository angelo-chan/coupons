package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.SellCouponGrantNotFoundException;
import com.chainz.coupon.shared.objects.SellCouponGrantInfo;

/** Sell coupon grant interface provide functionality to work with sell coupon grant. */
public interface SellCouponGrantService {

  /**
   * Get sell coupon grant.
   *
   * @param grantCode grant code.
   * @return sell coupon grant info.
   * @throws SellCouponGrantNotFoundException sell coupon grant not found.
   */
  SellCouponGrantInfo getSellCouponGrant(String grantCode) throws SellCouponGrantNotFoundException;

  /**
   * abort sell coupon grant.
   *
   * @param grantCode grant code.
   */
  void abortSellCouponGrant(String grantCode) throws SellCouponGrantNotFoundException;
}
