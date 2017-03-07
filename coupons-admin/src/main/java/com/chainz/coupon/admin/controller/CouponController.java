package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.CouponNotFoundException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.service.CouponService;
import com.chainz.coupon.shared.objects.CouponCreateRequest;
import com.chainz.coupon.shared.objects.CouponInfo;
import com.chainz.coupon.shared.objects.CouponIssuerType;
import com.chainz.coupon.shared.objects.CouponStatus;
import com.chainz.coupon.shared.objects.CouponUpdateRequest;
import com.chainz.coupon.shared.objects.GrantCode;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import com.chainz.coupon.shared.validator.common.EnumerationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/** Coupon controller. */
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

  @Autowired private CouponService couponService;

  /**
   * List coupons.
   *
   * @param issuerType coupon issuer type.
   * @param issuerId coupon issuer id.
   * @param status coupon status.
   * @param pageable coupon pagination.
   * @return paginated coupon info.
   */
  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public PaginatedApiResult<CouponInfo> listCoupon(
      @RequestParam(value = "issuerType", required = false)
          @EnumerationValidator(CouponIssuerType.class)
          CouponIssuerType issuerType,
      @RequestParam(value = "issuerId", required = false) String issuerId,
      @RequestParam(value = "status", required = false) @EnumerationValidator(CouponStatus.class)
          CouponStatus status,
      @RequestParam(value = "q", required = false) String q,
      @PageableDefault(
            value = 20,
            sort = {"id"},
            direction = Sort.Direction.DESC
          )
          Pageable pageable) {
    return couponService.listCoupon(issuerType, issuerId, status, q, pageable);
  }

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
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
  public CouponInfo getCoupon(@PathVariable Long id) throws CouponNotFoundException {
    return couponService.getCoupon(id);
  }

  /**
   * Update coupon.
   *
   * @param id coupon id.
   * @param couponUpdateRequest coupon update request.
   * @return coupon info.
   * @throws CouponNotFoundException coupon not found.
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
   * @throws CouponNotFoundException coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  @RequestMapping(
    value = "/{id}/verified",
    method = RequestMethod.PUT,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyCoupon(@PathVariable Long id)
      throws CouponNotFoundException, CouponStatusConflictException {
    couponService.verifyCoupon(id);
  }

  /**
   * Invalid coupon.
   *
   * @param id coupon id.
   * @throws CouponNotFoundException coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  @RequestMapping(
    value = "/{id}/invalid",
    method = RequestMethod.PUT,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void invalidCoupon(@PathVariable Long id)
      throws CouponNotFoundException, CouponStatusConflictException {
    couponService.invalidCoupon(id);
  }

  /**
   * Increase coupon circulation.
   *
   * @param id coupon id.
   * @param increment circulation increment.
   * @throws CouponNotFoundException coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  @RequestMapping(
    value = "/{id}/increment/{increment}",
    method = RequestMethod.PUT,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void increaseCouponCirculation(@PathVariable Long id, @PathVariable @Min(1) Long increment)
      throws CouponNotFoundException, CouponStatusConflictException {
    couponService.increaseCouponCirculation(id, increment);
  }

  /**
   * generate grant code for seller to scan to get sell coupon.
   *
   * @param id coupon id.
   * @param count coupon grant count.
   * @return grant code.
   */
  @RequestMapping(
    value = "/{id}/grant/{count}",
    method = RequestMethod.POST,
    produces = "application/json"
  )
  public GrantCode generateCouponGrantCode(@PathVariable Long id, @PathVariable Integer count) {
    return couponService.generateCouponGrantCode(id, count);
  }
}
