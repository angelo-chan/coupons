package com.chainz.coupon.admin.controller;

import com.chainz.coupon.core.exception.CouponInsufficientException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.SellCouponInsufficientException;
import com.chainz.coupon.core.exception.SellCouponNotFoundException;
import com.chainz.coupon.core.service.SellCouponService;
import com.chainz.coupon.shared.objects.GrantCode;
import com.chainz.coupon.shared.objects.SellCouponInfo;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/** Sell coupon controller. */
@RestController
@Validated
@RequestMapping("/api/sell-coupons")
public class SellCouponController {

  @Autowired private SellCouponService sellCouponService;

  /**
   * Enable seller to get sell coupon via grant code.
   *
   * @param grantCode grant code.
   * @throws InvalidGrantCodeException invalid grant code.
   * @throws CouponStatusConflictException coupon status conflict.
   * @throws CouponInsufficientException coupon sku insufficient.
   */
  @RequestMapping(
    value = "/granted/{grantCode}",
    method = RequestMethod.POST,
    consumes = "application/json"
  )
  @ResponseStatus(HttpStatus.CREATED)
  public void granted(@PathVariable String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException, CouponInsufficientException {
    sellCouponService.granted(grantCode);
  }

  /**
   * list seller's sell coupons.
   *
   * @param page coupon pagination page.
   * @param size coupon pagination size.
   * @param sort coupon pagination sort.
   * @param order coupon pagination order.
   * @return paginate sell coupon information.
   */
  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public PaginatedApiResult<SellCouponInfo> listSellCoupon(
      @Min(0) @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @Min(1) @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
      @Pattern(regexp = "id|createdAt")
          @RequestParam(value = "sort", required = false, defaultValue = "id")
          String sort,
      @Pattern(regexp = "asc|desc")
          @RequestParam(value = "order", required = false, defaultValue = "desc")
          String order) {
    return sellCouponService.listSellCoupon(
        new PageRequest(page, size, Sort.Direction.fromString(order), sort));
  }

  /**
   * Generate sell coupon grant code that user can use this code to get user coupon.
   *
   * @param id coupon id.
   * @param count sell-coupon grant count.
   * @return sell coupon grant code.
   * @throws SellCouponNotFoundException sell coupon not found.
   * @throws SellCouponInsufficientException sell coupon insufficient.
   * @throws CouponStatusConflictException coupon status conflict.
   */
  @RequestMapping(
    value = "/{id}/grant/{count}",
    method = RequestMethod.POST,
    produces = "application/json"
  )
  public GrantCode generateSellCouponGrantCode(
      @PathVariable Long id, @Min(1) @PathVariable Integer count)
      throws SellCouponNotFoundException, SellCouponInsufficientException,
          CouponStatusConflictException {
    return sellCouponService.generateSellCouponGrantCode(id, count);
  }
}
