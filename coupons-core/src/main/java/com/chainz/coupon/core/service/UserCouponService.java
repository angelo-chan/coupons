package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.SellCouponGrantInsufficientException;
import com.chainz.coupon.core.exception.SellCouponGrantStatusConflictException;
import com.chainz.coupon.core.exception.UserCouponNotFoundException;
import com.chainz.coupon.shared.objects.SimpleUserCouponInfo;
import com.chainz.coupon.shared.objects.UserCouponInfo;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import org.springframework.data.domain.Pageable;

/** user coupon interface provide functionality to work with user coupon. */
public interface UserCouponService {
  /**
   * Grant sell coupon to specific user.
   *
   * @param grantCode grant code.
   * @throws InvalidGrantCodeException invalid grant code.
   * @throws CouponStatusConflictException coupon status conflict.
   * @throws SellCouponGrantInsufficientException sell coupon insufficient.
   * @throws SellCouponGrantStatusConflictException sell coupon grant status conflict.
   */
  void granted(String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException,
          SellCouponGrantInsufficientException, SellCouponGrantStatusConflictException;

  /**
   * Get user coupon by id.
   *
   * @param userCouponId user coupon id.
   * @return user coupon info.
   * @throws UserCouponNotFoundException user coupon not found.
   */
  UserCouponInfo getUserCoupon(Long userCouponId) throws UserCouponNotFoundException;

  /**
   * List active user coupon.
   *
   * @param pageable pagination information.
   * @return simple user coupon info list.
   */
  PaginatedApiResult<SimpleUserCouponInfo> listActiveUserCoupon(Pageable pageable);

  /**
   * List expired user coupon.
   *
   * @param pageable pagination information.
   * @return simple user coupon info list.
   */
  PaginatedApiResult<SimpleUserCouponInfo> listExpiredUserCoupon(Pageable pageable);
}
