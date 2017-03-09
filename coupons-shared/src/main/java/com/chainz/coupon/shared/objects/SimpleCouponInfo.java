package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

/** Simple coupon info. */
@Getter
@Setter
public class SimpleCouponInfo {
  protected Long id;

  protected CouponType type;

  protected String title;

  protected String subtitle;

  protected String brandName;

  protected String color;

  protected Float value;

  protected Integer getLimit;

  protected CouponTarget target;

  protected CouponDateInfo dateInfo;
}
