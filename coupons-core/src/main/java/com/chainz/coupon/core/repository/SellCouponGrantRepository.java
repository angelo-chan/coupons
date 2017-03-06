package com.chainz.coupon.core.repository;

import com.chainz.coupon.core.model.SellCouponGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/** Sell coupon grant repository. */
public interface SellCouponGrantRepository
    extends JpaRepository<SellCouponGrant, String>, QueryDslPredicateExecutor<SellCouponGrant> {}
