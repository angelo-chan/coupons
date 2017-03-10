package com.chainz.coupon.core.repository;

import com.chainz.coupon.core.model.UserCouponShare;
import com.chainz.coupon.core.repository.common.JoinFetchCapableQueryDslJpaRepository;

/** User coupon share repository. */
public interface UserCouponShareRepository
    extends JoinFetchCapableQueryDslJpaRepository<UserCouponShare, Long> {}
