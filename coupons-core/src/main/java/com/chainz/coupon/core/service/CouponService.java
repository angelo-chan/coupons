package com.chainz.coupon.core.service;

import com.chainz.coupon.shared.objects.CouponCreateRequest;
import com.chainz.coupon.shared.objects.CouponInfo;
import com.chainz.coupon.shared.objects.CouponUpdateRequest;

/** Coupon core service for providing functionality to work with coupon. */
public interface CouponService {

  /**
   * Create a coupon.
   *
   * @param couponCreateRequest coupon create request
   * @return coupon information
   */
  CouponInfo createCoupon(CouponCreateRequest couponCreateRequest);

  /**
   * Update a coupon.
   *
   * @param id coupon id
   * @param couponUpdateRequest coupon update request
   * @return coupon information
   */
  CouponInfo updateCoupon(String id, CouponUpdateRequest couponUpdateRequest);

  /**
   * Verify coupon.
   *
   * @param id coupon id.
   */
  void verifyCoupon(String id);

  /**
   * Invalid coupon.
   *
   * @param id coupon id.
   */
  void invalidCoupon(String id);
}
