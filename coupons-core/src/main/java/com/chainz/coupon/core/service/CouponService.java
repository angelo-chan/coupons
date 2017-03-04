package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponNotFoundException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.shared.objects.CouponCreateRequest;
import com.chainz.coupon.shared.objects.CouponInfo;
import com.chainz.coupon.shared.objects.CouponUpdateRequest;

/**
 * Coupon core service for providing functionality to work with coupon.
 */
public interface CouponService {

  /**
   * Get a coupon
   *
   * @param id coupon id.
   * @return coupon information.
   * @throws CouponNotFoundException coupon not found.
   */
  CouponInfo getCoupon(Long id) throws CouponNotFoundException;

  /**
   * Create a coupon.
   *
   * @param couponCreateRequest coupon create request.
   * @return coupon information.
   */
  CouponInfo createCoupon(CouponCreateRequest couponCreateRequest);

  /**
   * Update a coupon.
   *
   * @param id                  coupon id
   * @param couponUpdateRequest coupon update request.
   * @return coupon information.
   * @throws CouponNotFoundException       coupon not found exception.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  CouponInfo updateCoupon(Long id, CouponUpdateRequest couponUpdateRequest)
    throws CouponNotFoundException, CouponStatusConflictException;

  /**
   * Verify coupon.
   *
   * @param id coupon id.
   * @throws CouponNotFoundException       coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  void verifyCoupon(Long id) throws CouponNotFoundException, CouponStatusConflictException;

  /**
   * Invalid coupon.
   *
   * @param id coupon id.
   * @throws CouponNotFoundException       coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  void invalidCoupon(Long id) throws CouponNotFoundException, CouponStatusConflictException;
}
