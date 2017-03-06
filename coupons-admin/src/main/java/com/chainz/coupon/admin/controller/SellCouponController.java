package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.CouponInsufficientException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.service.SellCouponService;
import com.chainz.coupon.shared.objects.SellCouponInfo;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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


  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public PaginatedApiResult<SellCouponInfo> list(
      @PageableDefault(
            value = 20,
            sort = {"id"},
            direction = Sort.Direction.DESC
          )
          Pageable pageable) {
    return sellCouponService.list(pageable);
  }
}
