package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.CouponGrantNotFoundException;
import com.chainz.coupon.core.service.CouponGrantService;
import com.chainz.coupon.shared.objects.CouponGrantInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** sell coupon grant controller. */
@RestController
@Api(tags = "Coupon-Grant", produces = "application/json", consumes = "application/json")
@Validated
@RequestMapping("/api/coupon-grants")
public class CouponGrantController {

  @Autowired
  private CouponGrantService couponGrantService;

  /**
   * Get sell coupon grant.
   *
   * @param grantCode grant code.
   * @return sell coupon grant information.
   * @throws CouponGrantNotFoundException coupon grant not found.
   */
  @ApiResponses({@ApiResponse(code = 404, message = "coupon grant not found")})
  @RequestMapping(value = "/{grantCode}", method = RequestMethod.GET, produces = "application/json")
  public CouponGrantInfo getCouponGrant(@PathVariable String grantCode)
    throws CouponGrantNotFoundException {
    return couponGrantService.getCouponGrant(grantCode);
  }
}
