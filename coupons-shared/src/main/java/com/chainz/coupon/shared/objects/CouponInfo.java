package com.chainz.coupon.shared.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/** Coupon info. */
@Getter
@Setter
public final class CouponInfo extends BulkCouponInfo {

  private Long circulation;

  private Long sku;

  private CouponStatus status;

  private CouponIssuer issuer;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private ZonedDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private ZonedDateTime updatedAt;
}
