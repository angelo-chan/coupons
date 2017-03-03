package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.CouponNotFoundException;
import com.chainz.coupon.core.service.CouponService;
import com.chainz.coupon.shared.objects.CouponCreateRequest;
import com.chainz.coupon.shared.objects.CouponInfo;
import com.chainz.coupon.shared.objects.CouponUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/** Coupon controller. */
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

  @Autowired private CouponService couponService;

  /**
   * Create coupon.
   *
   * @param couponCreateRequest coupon create request.
   * @return coupon info.
   */
  @RequestMapping(
    method = RequestMethod.POST,
    consumes = "application/json",
    produces = "application/json"
  )
  public CouponInfo createCoupon(@RequestBody @Valid CouponCreateRequest couponCreateRequest) {
    return couponService.createCoupon(couponCreateRequest);
  }

  /**
   * Update coupon.
   *
   * @param id coupon id.
   * @param couponUpdateRequest coupon update request.
   * @return coupon info.
   * @throws CouponNotFoundException coupon not found.
   */
  @RequestMapping(
    value = "/{id}",
    method = RequestMethod.PUT,
    consumes = "application/json",
    produces = "application/json"
  )
  public CouponInfo updateCoupon(
      @PathVariable String id, @RequestBody CouponUpdateRequest couponUpdateRequest)
      throws CouponNotFoundException {
    return couponService.updateCoupon(id, couponUpdateRequest);
  }

  /**
   * Verify coupon.
   *
   * @param id coupon id.
   * @throws CouponNotFoundException coupon not found.
   */
  @RequestMapping(
    value = "/{id}/verified",
    method = RequestMethod.PUT,
    consumes = "application/json"
  )
  public void verifyCoupon(@PathVariable String id) throws CouponNotFoundException {
    couponService.verifyCoupon(id);
  }

  /**
   * Invalid coupon.
   *
   * @param id coupon id.
   * @throws CouponNotFoundException coupon not found.
   */
  @RequestMapping(
    value = "/{id}/invalid",
    method = RequestMethod.PUT,
    consumes = "application/json"
  )
  public void invalidCoupon(@PathVariable String id) throws CouponNotFoundException {
    couponService.invalidCoupon(id);
  }
}
