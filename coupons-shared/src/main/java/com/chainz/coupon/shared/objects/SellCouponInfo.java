package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

/** Sell coupon info. */
@Getter
@Setter
public class SellCouponInfo {

  private Long id;

  private Integer sku;

  private SimpleCouponInfo coupon;
}
