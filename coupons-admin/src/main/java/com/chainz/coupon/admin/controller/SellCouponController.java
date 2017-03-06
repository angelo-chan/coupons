package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.CouponInsufficientException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.service.SellCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Sell coupon controller. */
@RestController
@RequestMapping("/api/sell-coupons")
public class SellCouponController {

  @Autowired private SellCouponService sellCouponService;

  /** Grant sell coupons. */
  @RequestMapping(
    value = "/grant/{grantCode}",
    method = RequestMethod.POST,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.CREATED)
  public void createGrant(@PathVariable String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException, CouponInsufficientException {
    sellCouponService.grant(grantCode);
  }
}
