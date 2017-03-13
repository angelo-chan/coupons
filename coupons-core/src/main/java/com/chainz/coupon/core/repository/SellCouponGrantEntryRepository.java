package com.chainz.coupon.core.repository;

import com.chainz.coupon.core.model.SellCouponGrantEntry;
import com.chainz.coupon.core.repository.common.JoinFetchCapableQueryDslJpaRepository;

/** Sell coupon grant entry entry repository. */
public interface SellCouponGrantEntryRepository
    extends JoinFetchCapableQueryDslJpaRepository<SellCouponGrantEntry, Long> {}
