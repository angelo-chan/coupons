package com.chainz.coupon.shared.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/** User coupon share grant. */
@Getter
@Setter
public class UserCouponShareInfo {

  private String id;

  private SimpleCouponInfo coupon;

  private String openId;

  private String userId;

  private Integer count;

  private Integer remain;

  private UserCouponShareStatus status;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private ZonedDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private ZonedDateTime expiredAt;
}
