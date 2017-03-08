package com.chainz.coupon.shared.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

/** User coupon info. */
@Getter
@Setter
public class UserCouponInfo {
  private Long id;

  private SimpleCouponInfo coupon;

  private String openId;

  private String userId;

  private String couponCode;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate beginDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  private OutId outId;

  private UserCouponStatus status = UserCouponStatus.UNUSED;

  private CouponType type;

  private String title;

  private String subtitle;

  private String brandName;

  private String color;

  private Float value;

  private CouponTarget target;

  private Boolean canShare = true;

  @Getter
  @Setter
  public static class SimpleCouponInfo {
    private Long id;

    private String description;

    private String notice;

    private String servicePhone;

    private Integer getLimit;

    private Set<String> stores;

    private CouponExtension extension;
  }
}
