package com.chainz.coupon.shared.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

/** Simple user coupon info. */
@Getter
@Setter
public class SimpleUserCouponInfo {

  private Long id;

  private SimpleCouponInfo coupon;

  private String openId;

  private String userId;

  private String couponCode;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate beginDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private ZonedDateTime gotAt;

  private UserCouponStatus status;
}
