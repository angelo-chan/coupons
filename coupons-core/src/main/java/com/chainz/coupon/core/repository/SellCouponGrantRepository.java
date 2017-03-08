package com.chainz.coupon.core.repository;

import com.chainz.coupon.core.model.SellCouponGrant;
import com.chainz.coupon.core.repository.common.JoinFetchCapableQueryDslJpaRepository;

/** Sell coupon grant repository. */
public interface SellCouponGrantRepository
    extends JoinFetchCapableQueryDslJpaRepository<SellCouponGrant, String> {}
