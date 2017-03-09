package com.chainz.coupon.shared.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/** User coupon info. */
@Getter
@Setter
public class UserCouponInfo {
  private Long id;

  private SupplementalCouponInfo coupon;

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
}
