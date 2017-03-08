package com.chainz.coupon.core.repository;

import com.chainz.coupon.core.model.UserCoupon;
import com.chainz.coupon.core.repository.common.JoinFetchCapableQueryDslJpaRepository;

/** user coupon repository. */
public interface UserCouponRepository
    extends JoinFetchCapableQueryDslJpaRepository<UserCoupon, Long> {

  /**
   * Check exists by coupon code.
   *
   * @param couponCode coupon code.
   * @return exists or not.
   */
  boolean existsByCouponCode(String couponCode);
}
