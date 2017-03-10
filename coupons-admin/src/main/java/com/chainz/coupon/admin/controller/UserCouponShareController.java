package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.UserCouponShareNotFoundException;
import com.chainz.coupon.core.service.UserCouponShareService;
import com.chainz.coupon.shared.objects.UserCouponShareInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** user coupon share controller. */
@RestController
@Validated
@RequestMapping("/api/user-coupon-shares")
public class UserCouponShareController {

  @Autowired private UserCouponShareService userCouponShareService;

  /**
   * Get user coupon share.
   *
   * @param shareCode share code.
   * @return user coupon share information.
   * @throws UserCouponShareNotFoundException user coupon share not found.
   */
  @RequestMapping(value = "/{shareCode}", method = RequestMethod.GET, produces = "application/json")
  public UserCouponShareInfo getSellCouponGrant(@PathVariable String shareCode)
      throws UserCouponShareNotFoundException {
    return userCouponShareService.getUserCouponShare(shareCode);
  }
}
