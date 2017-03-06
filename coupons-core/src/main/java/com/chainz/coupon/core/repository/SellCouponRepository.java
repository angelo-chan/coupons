package com.chainz.coupon.core.repository;

import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.model.SellCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/** Sell coupon repository. */
public interface SellCouponRepository
    extends JpaRepository<SellCoupon, Long>, QueryDslPredicateExecutor<SellCoupon> {

  SellCoupon findOneByOpenIdAndCoupon(String openId, Coupon coupon);
}
