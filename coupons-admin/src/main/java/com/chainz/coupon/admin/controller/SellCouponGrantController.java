package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.SellCouponGrantNotFoundException;
import com.chainz.coupon.core.exception.SellCouponGrantStatusConflictException;
import com.chainz.coupon.core.service.SellCouponGrantService;
import com.chainz.coupon.shared.objects.SellCouponGrantInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** sell coupon grant controller. */
@RestController
@Api(tags = "Sell-Coupon-Grant", produces = "application/json", consumes = "application/json")
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
  @ApiResponses({@ApiResponse(code = 404, message = "sell coupon grant not found")})
  @RequestMapping(value = "/{grantCode}", method = RequestMethod.GET, produces = "application/json")
  public SellCouponGrantInfo getSellCouponGrant(@PathVariable String grantCode)
      throws SellCouponGrantNotFoundException {
    return sellCouponGrantService.getSellCouponGrant(grantCode);
  }

  /**
   * Abort sell coupon grant.
   *
   * @param grantCode grant code.
   * @throws SellCouponGrantNotFoundException sell coupon grant not found.
   * @throws SellCouponGrantStatusConflictException sell coupon grant status conflict.
   */
  @ApiResponses({
    @ApiResponse(code = 404, message = "sell coupon grant not found"),
    @ApiResponse(code = 409, message = "sell coupon grant status conflict")
  })
  @RequestMapping(
    value = "/{grantCode}/aborted",
    method = RequestMethod.POST,
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void abortSellCouponGrant(@PathVariable String grantCode)
      throws SellCouponGrantNotFoundException, SellCouponGrantStatusConflictException {
    sellCouponGrantService.abortSellCouponGrant(grantCode);
  }
}
