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

  @Getter
  @Setter
  public static class SimpleCouponInfo {
    private Long id;

    private CouponType type;

    private String title;

    private String subtitle;

    private String brandName;

    private String description;

    private String color;

    private Float value;

    private Integer getLimit;

    private CouponTarget target;

    private CouponDateInfo dateInfo;
  }
}
