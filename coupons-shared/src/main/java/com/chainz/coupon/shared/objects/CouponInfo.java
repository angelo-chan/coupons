package com.chainz.coupon.shared.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
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

  private Long circulation;

  private Long sku;

  private Float value;

  private Integer getLimit;

  private CouponTarget target;

  private Set<String> stores = new HashSet<>();

  private CouponStatus status;

  private CouponExtension extension;

  private CouponIssuer issuer;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private ZonedDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private ZonedDateTime updatedAt;

}
