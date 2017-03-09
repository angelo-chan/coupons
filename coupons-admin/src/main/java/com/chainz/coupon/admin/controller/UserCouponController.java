package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.SellCouponGrantInsufficientException;
import com.chainz.coupon.core.exception.SellCouponGrantStatusConflictException;
import com.chainz.coupon.core.exception.UserCouponNotFoundException;
import com.chainz.coupon.core.service.UserCouponService;
import com.chainz.coupon.shared.objects.UserCouponInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** user coupon controller. */
@RestController
@Validated
@RequestMapping("/api/user-coupons")
public class UserCouponController {

  @Autowired private UserCouponService userCouponService;

  /**
   * Enable user to get coupon via grant code from seller.
   *
   * @param grantCode grant code.
   * @throws InvalidGrantCodeException invalid grant code.
   * @throws CouponStatusConflictException coupon status conflict.
   * @throws SellCouponGrantInsufficientException sell coupon grant insufficient.
   * @throws SellCouponGrantStatusConflictException sell coupon grant status conflict.
   */
  @RequestMapping(
    value = "/granted/{grantCode}",
    method = RequestMethod.POST,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.CREATED)
  public void granted(@PathVariable String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException,
          SellCouponGrantInsufficientException, SellCouponGrantStatusConflictException {
    userCouponService.granted(grantCode);
  }

  /**
   * Get user coupon.
   *
   * @param id user coupon id.
   * @return user coupon information.
   * @throws UserCouponNotFoundException user coupon not found.
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
  public UserCouponInfo getUserCoupon(@PathVariable Long id) throws UserCouponNotFoundException {
    return userCouponService.getUserCoupon(id);
  }
}
