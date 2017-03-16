package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.CouponInsufficientException;
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

/** Coupon controller. */
@RestController
@Api(tags = "Coupon", produces = "application/json", consumes = "application/json")
@Validated
@RequestMapping("/api/coupons")
public class CouponController {

  @Autowired private CouponService couponService;

  /**
   * List coupons.
   *
   * @param issuerType coupon issuer type.
   * @param issuerId coupon issuer id.
   * @param status coupon status.
   * @param page coupon pagination page.
   * @param size coupon pagination size.
   * @param sort coupon pagination sort.
   * @param order coupon pagination order.
   * @return paginated coupon info.
   */
  @ApiImplicitParams({
    @ApiImplicitParam(
      name = "issuerType",
      value = "issuer type",
      dataType = "string",
      paramType = "query",
      allowableValues = "SYSTEM,VENDOR"
    ),
    @ApiImplicitParam(
      name = "status",
      value = "status",
      dataType = "string",
      paramType = "query",
      allowableValues = "UNVERIFIED,VERIFIED,INVALID"
    ),
    @ApiImplicitParam(
      name = "sort",
      value = "sort",
      dataType = "string",
      paramType = "query",
      allowableValues = "id,createdAt",
      defaultValue = "id"
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
  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public PaginatedApiResult<CouponInfo> listCoupon(
      @Pattern(regexp = "SYSTEM|VENDOR") @RequestParam(value = "issuerType", required = false)
          String issuerType,
      @RequestParam(value = "issuerId", required = false) String issuerId,
      @Pattern(regexp = "UNVERIFIED|VERIFIED|INVALID")
          @RequestParam(value = "status", required = false)
          String status,
      @RequestParam(value = "q", required = false) String q,
      @Min(0) @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @Min(1) @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
      @Pattern(regexp = "id|createdAt")
          @RequestParam(value = "sort", required = false, defaultValue = "id")
          String sort,
      @Pattern(regexp = "asc|desc")
          @RequestParam(value = "order", required = false, defaultValue = "desc")
          String order) {
    return couponService.listCoupon(
        issuerType != null ? CouponIssuerType.valueOf(issuerType) : null,
        issuerId,
        status != null ? CouponStatus.valueOf(status) : null,
        q,
        new PageRequest(page, size, Sort.Direction.fromString(order), sort));
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
  @ApiResponses(@ApiResponse(code = 404, message = "coupon not found"))
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
  @ApiResponses({
    @ApiResponse(code = 404, message = "coupon not found"),
    @ApiResponse(code = 409, message = "coupon status conflict")
  })
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
  @ApiResponses({
    @ApiResponse(code = 404, message = "coupon not found"),
    @ApiResponse(code = 409, message = "coupon status conflict")
  })
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
  @ApiResponses({
    @ApiResponse(code = 404, message = "coupon not found"),
    @ApiResponse(code = 409, message = "coupon status conflict")
  })
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
  @ApiResponses({
    @ApiResponse(code = 404, message = "coupon not found"),
    @ApiResponse(code = 409, message = "coupon status conflict")
  })
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
   * @throws CouponNotFoundException coupon not found.
   * @throws CouponStatusConflictException coupon status conflict.
   * @throws CouponInsufficientException coupon insufficient.
   */
  @ApiResponses({
    @ApiResponse(code = 404, message = "coupon not found"),
    @ApiResponse(code = 409, message = "coupon status conflict or coupon insufficient")
  })
  @RequestMapping(
    value = "/{id}/grant/{count}",
    method = RequestMethod.POST,
    produces = "application/json"
  )
  public GrantCode generateCouponGrantCode(
      @PathVariable @Min(1) Long id, @PathVariable Integer count)
      throws CouponNotFoundException, CouponStatusConflictException, CouponInsufficientException {
    return couponService.generateCouponGrantCode(id, count);
  }
}
