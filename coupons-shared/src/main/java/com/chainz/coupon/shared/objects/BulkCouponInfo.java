package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

/** Bulk coupon info. */
@Getter
@Setter
public class BulkCouponInfo extends SimpleCouponInfo {

  protected String description;

  protected String notice;

  protected CouponExtension extension;
}
