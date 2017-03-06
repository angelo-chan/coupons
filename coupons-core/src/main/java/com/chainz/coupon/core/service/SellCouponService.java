package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponInsufficientException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.shared.objects.SellCouponInfo;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import org.springframework.data.domain.Pageable;

/** Sell coupon interface. */
public interface SellCouponService {

  /**
   * Grant coupon to seller.
   *
   * @param grantCode grant code.
   * @throws InvalidGrantCodeException invalid grant code.
   * @throws CouponStatusConflictException coupon status conflict.
   * @throws CouponInsufficientException coupon insufficient.
   */
  void grant(String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException, CouponInsufficientException;

  /**
   * List sell coupon.
   *
   * @param pageable pagination information.
   * @return paginated sell coupon.
   */
  PaginatedApiResult<SellCouponInfo> list(Pageable pageable);
}
