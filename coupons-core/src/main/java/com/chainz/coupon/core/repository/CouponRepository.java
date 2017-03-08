package com.chainz.coupon.core.repository;

import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.repository.common.JoinFetchCapableQueryDslJpaRepository;

/** Coupon repository. */
public interface CouponRepository extends JoinFetchCapableQueryDslJpaRepository<Coupon, Long> {}
