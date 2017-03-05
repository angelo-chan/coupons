package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponNotFoundException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.shared.objects.CouponCreateRequest;
import com.chainz.coupon.shared.objects.CouponInfo;
import com.chainz.coupon.shared.objects.CouponIssuerType;
import com.chainz.coupon.shared.objects.CouponStatus;
import com.chainz.coupon.shared.objects.CouponUpdateRequest;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import org.springframework.data.domain.Pageable;

/** Coupon core service for providing functionality to work with coupon. */
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
   * @param id coupon id
   * @param couponUpdateRequest coupon update request.
   * @return coupon information.
   * @throws CouponNotFoundException coupon not found exception.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  CouponInfo updateCoupon(Long id, CouponUpdateRequest couponUpdateRequest)
      throws CouponNotFoundException, CouponStatusConflictException;

  /**
   * Verify coupon.
   *
   * @param id coupon id.
   * @throws CouponNotFoundException coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  void verifyCoupon(Long id) throws CouponNotFoundException, CouponStatusConflictException;

  /**
   * Invalid coupon.
   *
   * @param id coupon id.
   * @throws CouponNotFoundException coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  void invalidCoupon(Long id) throws CouponNotFoundException, CouponStatusConflictException;

  /**
   * Increase coupon circulation.
   *
   * @param id coupon id.
   * @param increment circulation increment.
   * @throws CouponNotFoundException coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  void increaseCirculation(Long id, Long increment)
      throws CouponNotFoundException, CouponStatusConflictException;

  /**
   * List coupon.
   *
   * @param issuerType issuer type.
   * @param issuerId issuer id.
   * @param status coupon status.
   * @param pageable pagnation information.
   * @return paginated coupon info.
   */
  PaginatedApiResult<CouponInfo> list(
      CouponIssuerType issuerType, String issuerId, CouponStatus status, Pageable pageable);
}
