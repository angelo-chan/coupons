package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponGetLimitException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.InvalidShareCodeException;
import com.chainz.coupon.core.exception.UserCouponNotFoundException;
import com.chainz.coupon.shared.objects.ShareCode;
import com.chainz.coupon.shared.objects.SimpleUserCouponInfo;
import com.chainz.coupon.shared.objects.UserCouponConsumeRequest;
import com.chainz.coupon.shared.objects.UserCouponInfo;
import com.chainz.coupon.shared.objects.UserCouponReturnRequest;
import com.chainz.coupon.shared.objects.UserCouponShareRequest;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import org.springframework.data.domain.Pageable;

/** user coupon interface provide functionality to work with user coupon. */
public interface UserCouponService {
  /**
   * get user coupon from sell coupon grant by grant code.
   *
   * @param grantCode grant code.
   * @throws InvalidGrantCodeException invalid grant code.
   * @throws CouponStatusConflictException coupon status conflict.
   * @throws CouponGetLimitException coupon get limit.
   */
  void granted(String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException, CouponGetLimitException;

  /**
   * get user coupon from other user by share code.
   *
   * @param shareCode share code.
   * @throws InvalidShareCodeException invalid share code.
   * @throws CouponGetLimitException coupon get limit exception.
   */
  void shared(String shareCode) throws InvalidShareCodeException, CouponGetLimitException;

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

  /**
   * Consume user coupons.
   *
   * @param userCouponConsumeRequest user coupon .
   * @throws UserCouponNotFoundException user coupon not found.
   */
  void consumeUserCoupon(UserCouponConsumeRequest userCouponConsumeRequest)
      throws UserCouponNotFoundException;

  /**
   * Return user coupons.
   *
   * @param userCouponReturnRequest user coupon return request.
   * @throws UserCouponNotFoundException user coupon not found.
   */
  void returnUserCoupon(UserCouponReturnRequest userCouponReturnRequest)
      throws UserCouponNotFoundException;

  /**
   * Share user coupons.
   *
   * @param userCouponShareRequest user coupon share request.
   * @return share code.
   * @throws UserCouponNotFoundException user coupon not found.
   */
  ShareCode shareUserCoupon(UserCouponShareRequest userCouponShareRequest)
      throws UserCouponNotFoundException;
}
