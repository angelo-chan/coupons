package com.chainz.coupon.core.repository;

import com.chainz.coupon.core.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/** Coupon repository. */
public interface CouponRepository
    extends JpaRepository<Coupon, Long>, QueryDslPredicateExecutor<Coupon> {}
