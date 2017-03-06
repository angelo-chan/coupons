package com.chainz.coupon.shared.objects;

import lombok.Data;

/** Sell coupon info. */
@Data
public class SellCouponInfo {

  private Long id;

  private Integer sku;

  private SimpleCouponInfo coupon;

  @Data
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
