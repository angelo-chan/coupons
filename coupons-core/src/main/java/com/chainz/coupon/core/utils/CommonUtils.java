package com.chainz.coupon.core.utils;

import com.chainz.coupon.core.exception.CouponExpiredException;
import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.model.CouponDateInfo;
import com.chainz.coupon.shared.objects.CouponDateType;

import java.time.LocalDate;

/** Common utils for coupons. */
public class CommonUtils {

  /**
   * Check coupon expired or not.
   *
   * @param coupon coupon
   * @throws CouponExpiredException coupon expired.
   */
  public static void checkCouponExpired(Coupon coupon) throws CouponExpiredException {
    LocalDate now = LocalDate.now();
    CouponDateInfo couponDateInfo = coupon.getDateInfo();
    if (couponDateInfo.getDateType() == CouponDateType.DATE_TYPE_FIXED_TIME_RANGE
        && couponDateInfo.getTimeRangeEnd().isBefore(now)) {
      throw new CouponExpiredException(coupon.getId());
    }
  }
}
