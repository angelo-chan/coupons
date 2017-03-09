package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/** Supplemental coupon info. */
@Getter
@Setter
public class SupplementalCouponInfo {
  protected Long id;

  protected String description;

  protected String notice;

  protected String servicePhone;

  protected Integer getLimit;

  protected Set<String> stores;

  protected CouponExtension extension;
}
