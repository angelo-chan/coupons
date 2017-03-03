package com.chainz.coupon.shared.objects;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Coupon info.
 */
@Data
public class CouponInfo {

  private Long id;

  private CouponType type;

  private String title;

  private String subtitle;

  private String brandName;

  private String description;

  private String color;

  private CouponDateInfo dateInfo;

  private String notice;

  private String servicePhone;

  private Boolean canShare;

  private Integer circulation;

  private Integer sku;

  private Float value;

  private Integer getLimit;

  private CouponTarget target;

  private Set<String> stores = new HashSet<>();

  private CouponStatus status;

  private CouponExtension extension;

  private CouponIssuer issuer;

  private ZonedDateTime createdAt;

  private ZonedDateTime updatedAt;
}
