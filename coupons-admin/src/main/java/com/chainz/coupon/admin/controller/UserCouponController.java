package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.CouponExpiredException;
import com.chainz.coupon.core.exception.CouponGetLimitException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.InvalidShareCodeException;
import com.chainz.coupon.core.exception.UserCouponNotFoundException;
import com.chainz.coupon.core.service.UserCouponService;
import com.chainz.coupon.shared.objects.ShareCode;
import com.chainz.coupon.shared.objects.SimpleUserCouponInfo;
import com.chainz.coupon.shared.objects.UserCouponConsumeRequest;
import com.chainz.coupon.shared.objects.UserCouponInfo;
import com.chainz.coupon.shared.objects.UserCouponReturnRequest;
import com.chainz.coupon.shared.objects.UserCouponShareRequest;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/** user coupon controller. */
@RestController
@Api(tags = "User-Coupon", produces = "application/json", consumes = "application/json")
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
   * @throws CouponGetLimitException coupon get limit.
   * @throws CouponExpiredException coupon expired.
   */
  @ApiResponses({
    @ApiResponse(code = 404, message = "invalid grant code"),
    @ApiResponse(
      code = 409,
      message = "coupon status conflict or coupon get limit or coupon expired"
    )
  })
  @RequestMapping(
    value = "/granted/{grantCode}",
    method = RequestMethod.POST,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.CREATED)
  public void granted(@PathVariable String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException, CouponGetLimitException,
          CouponExpiredException {
    userCouponService.granted(grantCode);
  }

  /**
   * Enable user to get coupon via share code from other user.
   *
   * @param shareCode grant code.
   * @throws InvalidShareCodeException invalid share code.
   * @throws CouponGetLimitException coupon get limit.
   */
  @ApiResponses({
    @ApiResponse(code = 404, message = "invalid share code"),
    @ApiResponse(code = 409, message = "coupon get limit")
  })
  @RequestMapping(
    value = "/shared/{shareCode}",
    method = RequestMethod.POST,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.CREATED)
  public void shared(@PathVariable String shareCode)
      throws InvalidShareCodeException, CouponGetLimitException {
    userCouponService.shared(shareCode);
  }

  /**
   * List active user coupon.
   *
   * @param page coupon pagination page.
   * @param size coupon pagination size.
   * @param sort coupon pagination sort, gotAt for default.
   * @param order coupon pagination order, desc for default.
   * @return active user coupon list.
   */
  @ApiImplicitParams({
    @ApiImplicitParam(
      name = "sort",
      value = "sort",
      dataType = "string",
      paramType = "query",
      allowableValues = "gotAt,endDate",
      defaultValue = "gotAt"
    ),
    @ApiImplicitParam(
      name = "order",
      value = "order",
      dataType = "string",
      paramType = "query",
      allowableValues = "asc,desc",
      defaultValue = "desc"
    )
  })
  @RequestMapping(value = "/active", method = RequestMethod.GET, produces = "application/json")
  public PaginatedApiResult<SimpleUserCouponInfo> listActiveUserCoupon(
      @Min(0) @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @Min(1) @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
      @Pattern(regexp = "gotAt|endDate")
          @RequestParam(value = "sort", required = false, defaultValue = "gotAt")
          String sort,
      @Pattern(regexp = "asc|desc")
          @RequestParam(value = "order", required = false, defaultValue = "desc")
          String order) {
    return userCouponService.listActiveUserCoupon(
        new PageRequest(page, size, Sort.Direction.fromString(order), sort));
  }

  /**
   * List usable user coupon.
   *
   * @param store order store id.
   * @return usable user coupon list.
   */
  @RequestMapping(value = "/usable", method = RequestMethod.GET, produces = "application/json")
  public List<SimpleUserCouponInfo> listUsableUserCoupon(
      @Size(min = 1) @RequestParam(value = "store") String store) {
    return userCouponService.listUsableUserCoupon(store);
  }

  /**
   * List expired user coupon.
   *
   * @param page coupon pagination page.
   * @param size coupon pagination size.
   * @return expired user coupon list.
   */
  @ApiImplicitParams({
    @ApiImplicitParam(
      name = "sort",
      value = "sort",
      dataType = "string",
      paramType = "query",
      allowableValues = "gotAt,endDate",
      defaultValue = "gotAt"
    ),
    @ApiImplicitParam(
      name = "order",
      value = "order",
      dataType = "string",
      paramType = "query",
      allowableValues = "asc,desc",
      defaultValue = "desc"
    )
  })
  @RequestMapping(value = "/expired", method = RequestMethod.GET, produces = "application/json")
  public PaginatedApiResult<SimpleUserCouponInfo> listExpiredUserCoupon(
      @Min(0) @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @Min(1) @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
      @Pattern(regexp = "gotAt|endDate")
          @RequestParam(value = "sort", required = false, defaultValue = "endDate")
          String sort,
      @Pattern(regexp = "asc|desc")
          @RequestParam(value = "order", required = false, defaultValue = "desc")
          String order) {
    return userCouponService.listExpiredUserCoupon(
        new PageRequest(page, size, Sort.Direction.fromString(order), sort));
  }

  /**
   * Consume user coupon.
   *
   * @param userCouponConsumeRequest user coupons consume request.
   * @throws UserCouponNotFoundException user coupon not found.
   */
  @ApiResponses({@ApiResponse(code = 404, message = "user coupon not found")})
  @RequestMapping(
    value = "/consume",
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void consumeUserCoupons(
      @RequestBody @Valid UserCouponConsumeRequest userCouponConsumeRequest)
      throws UserCouponNotFoundException {
    userCouponService.consumeUserCoupon(userCouponConsumeRequest);
  }

  /**
   * Return user coupon.
   *
   * @param userCouponReturnRequest user coupons return request.
   * @throws UserCouponNotFoundException user coupon not found.
   */
  @ApiResponses({
    @ApiResponse(code = 404, message = "user coupon not found"),
  })
  @RequestMapping(
    value = "/return",
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void returnUserCoupons(@RequestBody @Valid UserCouponReturnRequest userCouponReturnRequest)
      throws UserCouponNotFoundException {
    userCouponService.returnUserCoupon(userCouponReturnRequest);
  }

  /**
   * Share user coupons.
   *
   * @param userCouponShareRequest user coupon share request.
   * @return share code
   * @throws UserCouponNotFoundException user coupon not found exception.
   */
  @RequestMapping(
    value = "/share",
    method = RequestMethod.POST,
    consumes = "application/json",
    produces = "application/json"
  )
  @ApiResponses({@ApiResponse(code = 404, message = "user coupon not found")})
  public ShareCode shareUserCoupon(
      @RequestBody @Valid UserCouponShareRequest userCouponShareRequest)
      throws UserCouponNotFoundException {
    return userCouponService.shareUserCoupon(userCouponShareRequest);
  }

  /**
   * Get user coupon.
   *
   * @param id user coupon id.
   * @return user coupon information.
   * @throws UserCouponNotFoundException user coupon not found.
   */
  @ApiResponses({@ApiResponse(code = 404, message = "user coupon not found")})
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
  public UserCouponInfo getUserCoupon(@PathVariable Long id) throws UserCouponNotFoundException {
    return userCouponService.getUserCoupon(id);
  }
}
