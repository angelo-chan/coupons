package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.CouponNotFoundException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.service.CouponService;
import com.chainz.coupon.shared.objects.CouponCreateRequest;
import com.chainz.coupon.shared.objects.CouponInfo;
import com.chainz.coupon.shared.objects.CouponUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Coupon controller.
 */
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

  @Autowired
  private CouponService couponService;

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
  @ResponseStatus(HttpStatus.CREATED)
  public CouponInfo createCoupon(@RequestBody @Valid CouponCreateRequest couponCreateRequest) {
    return couponService.createCoupon(couponCreateRequest);
  }


  /**
   * Get coupon.
   *
   * @param id coupon id.
   * @return coupon information.
   * @throws CouponNotFoundException coupon not found.
   */
  @RequestMapping(
    value = "/{id}",
    method = RequestMethod.GET,
    produces = "application/json"
  )
  public CouponInfo getCoupon(@PathVariable Long id) throws CouponNotFoundException {
    return couponService.getCoupon(id);
  }

  /**
   * Update coupon.
   *
   * @param id                  coupon id.
   * @param couponUpdateRequest coupon update request.
   * @return coupon info.
   * @throws CouponNotFoundException       coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  @RequestMapping(
    value = "/{id}",
    method = RequestMethod.PUT,
    consumes = "application/json",
    produces = "application/json"
  )
  public CouponInfo updateCoupon(
    @PathVariable Long id, @RequestBody @Valid CouponUpdateRequest couponUpdateRequest)
    throws CouponNotFoundException, CouponStatusConflictException {
    return couponService.updateCoupon(id, couponUpdateRequest);
  }

  /**
   * Verify coupon.
   *
   * @param id coupon id.
   * @throws CouponNotFoundException       coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  @RequestMapping(
    value = "/{id}/verified",
    method = RequestMethod.PUT,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyCoupon(@PathVariable Long id) throws CouponNotFoundException, CouponStatusConflictException {
    couponService.verifyCoupon(id);
  }

  /**
   * Invalid coupon.
   *
   * @param id coupon id.
   * @throws CouponNotFoundException       coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  @RequestMapping(
    value = "/{id}/invalid",
    method = RequestMethod.PUT,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void invalidCoupon(@PathVariable Long id) throws CouponNotFoundException, CouponStatusConflictException {
    couponService.invalidCoupon(id);
  }

  /**
   * Increase coupon circulation.
   *
   * @param id        coupon id.
   * @param increment circulation increment.
   * @throws CouponNotFoundException       coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  @RequestMapping(
    value = "/{id}/increment/{increment}",
    method = RequestMethod.PUT,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void increaseCirculation(@PathVariable Long id, @PathVariable @Min(1) Long increment)
    throws CouponNotFoundException, CouponStatusConflictException {
    couponService.increaseCirculation(id, increment);
  }
}
