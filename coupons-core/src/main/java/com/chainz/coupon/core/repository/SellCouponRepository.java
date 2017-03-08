package com.chainz.coupon.core.repository;

import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.model.SellCoupon;
import com.chainz.coupon.core.repository.common.JoinFetchCapableQueryDslJpaRepository;

/** Sell coupon repository. */
public interface SellCouponRepository
    extends JoinFetchCapableQueryDslJpaRepository<SellCoupon, Long> {

  SellCoupon findOneByOpenIdAndCoupon(String openId, Coupon coupon);
}
