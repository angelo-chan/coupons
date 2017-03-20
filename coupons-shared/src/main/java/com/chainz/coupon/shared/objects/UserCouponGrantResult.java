package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCouponGrantResult {

  private Integer count;

  private Integer remain;

  private SimpleUserCouponInfo userCoupon;
}
