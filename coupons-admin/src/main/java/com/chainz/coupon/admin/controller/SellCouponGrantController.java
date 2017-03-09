package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.SellCouponGrantNotFoundException;
import com.chainz.coupon.core.service.SellCouponGrantService;
import com.chainz.coupon.shared.objects.SellCouponGrantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** sell coupon grant controller. */
@RestController
@Validated
@RequestMapping("/api/sell-coupon-grants")
public class SellCouponGrantController {

  @Autowired private SellCouponGrantService sellCouponGrantService;

  /**
   * Get sell coupon grant.
   *
   * @param grantCode grant code.
   * @return sell coupon grant information.
   * @throws SellCouponGrantNotFoundException sell coupon grant not found.
   */
  @RequestMapping(value = "/{grantCode}", method = RequestMethod.GET, produces = "application/json")
  public SellCouponGrantInfo getSellCouponGrant(@PathVariable String grantCode)
      throws SellCouponGrantNotFoundException {
    return sellCouponGrantService.getSellCouponGrant(grantCode);
  }
}
