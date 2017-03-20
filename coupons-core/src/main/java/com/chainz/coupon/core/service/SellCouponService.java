package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponExpiredException;
import com.chainz.coupon.core.exception.CouponInsufficientException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.SellCouponInsufficientException;
import com.chainz.coupon.core.exception.SellCouponNotFoundException;
import com.chainz.coupon.shared.objects.BulkSellCouponInfo;
import com.chainz.coupon.shared.objects.GrantCode;
import com.chainz.coupon.shared.objects.SimpleSellCouponInfo;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import org.springframework.data.domain.Pageable;

/** Sell coupon interface provide functionality to work with sell coupon. */
public interface SellCouponService {

  /**
   * Grant coupon to seller.
   *
   * @param grantCode grant code.
   * @throws InvalidGrantCodeException invalid grant code.
   * @throws CouponStatusConflictException coupon status conflict.
   * @throws CouponInsufficientException coupon insufficient.
   * @throws CouponExpiredException coupon expired.
   */
  void granted(String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException, CouponInsufficientException,
          CouponExpiredException;

  /**
   * List sell coupon.
   *
   * @param pageable pagination information.
   * @return paginated sell coupon.
   */
  PaginatedApiResult<SimpleSellCouponInfo> listSellCoupon(Pageable pageable);

  /**
   * Generate sell coupon grant code.
   *
   * @param id sell coupon id.
   * @param count sell coupon grant code.
   * @return grant code.
   * @throws SellCouponNotFoundException sell coupon not found.
   * @throws SellCouponInsufficientException sell coupon insufficient.
   * @throws CouponExpiredException coupon expired exception.
   */
  GrantCode generateSellCouponGrantCode(Long id, Integer count)
      throws SellCouponNotFoundException, SellCouponInsufficientException,
          CouponStatusConflictException, CouponExpiredException;

  /**
   * Get sell couponsell by id.
   *
   * @param sellCouponId sell coupon id.
   * @return sellCoupon info.
   * @throws SellCouponNotFoundException user coupon not found.
   */
  BulkSellCouponInfo getSellCoupon(Long sellCouponId) throws SellCouponNotFoundException;
}
