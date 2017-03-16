package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.UserCouponShareNotFoundException;
import com.chainz.coupon.core.exception.UserCouponShareStatusConflictException;
import com.chainz.coupon.core.service.UserCouponShareService;
import com.chainz.coupon.shared.objects.UserCouponShareInfo;
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

/** user coupon share controller. */
@RestController
@Api(tags = "User-Coupon-Share", produces = "application/json", consumes = "application/json")
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
  @ApiResponses({@ApiResponse(code = 404, message = "user coupon share not found")})
  @RequestMapping(value = "/{shareCode}", method = RequestMethod.GET, produces = "application/json")
  public UserCouponShareInfo getSellCouponGrant(@PathVariable String shareCode)
      throws UserCouponShareNotFoundException {
    return userCouponShareService.getUserCouponShare(shareCode);
  }

  /**
   * Abort user coupon share.
   *
   * @param shareCode share code.
   * @throws UserCouponShareNotFoundException user coupon share not found.
   * @throws UserCouponShareStatusConflictException user coupon share status conflict.
   */
  @ApiResponses({
    @ApiResponse(code = 404, message = "user coupon share not found"),
    @ApiResponse(code = 409, message = "user coupon share status conflict")
  })
  @RequestMapping(
    value = "/{shareCode}/aborted",
    method = RequestMethod.POST,
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void abortUserCouponShare(@PathVariable String shareCode)
      throws UserCouponShareNotFoundException, UserCouponShareStatusConflictException {
    userCouponShareService.abortUserCouponShare(shareCode);
  }
}
