package com.chainz.coupon.shared.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

/** User coupon info. */
@Getter
@Setter
public class UserCouponInfo {
  private Long id;

  private CouponInfo coupon;

  private String openId;

  private String userId;

  private String couponCode;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate beginDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private ZonedDateTime gotAt;

  private OutId outId;

  private UserCouponStatus status = UserCouponStatus.UNUSED;
}
