package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

/** Simple sell coupon info. */
@Getter
@Setter
public class SimpleSellCouponInfo {
  protected Long id;

  protected Integer sku;

  protected SimpleCouponInfo coupon;
}
