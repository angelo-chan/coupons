package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/** Simple coupon info. */
@Getter
@Setter
public class SimpleCouponInfo {
  protected Long id;

  protected CouponType type;

  protected String title;

  protected String subtitle;

  protected String brandName;

  private Boolean canShare;

  protected String color;

  protected Float value;

  protected Integer getLimit;

  protected CouponTarget target;

  protected CouponDateInfo dateInfo;

  protected Set<String> stores;
}
