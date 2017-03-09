package com.chainz.coupon.shared.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/** Sell coupon grant info. */
@Getter
@Setter
public class SellCouponGrantInfo {

  private String id;

  private SimpleSellCouponInfo sellCoupon;

  private String openId;

  private Integer count;

  private Integer remain;

  private SellCouponGrantStatus status;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private ZonedDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private ZonedDateTime expiredAt;
}
