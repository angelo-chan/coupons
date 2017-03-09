package com.chainz.coupon.shared.objects;

import com.chainz.coupon.shared.validator.common.EnumerationValidator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/** Coupon update request. */
@Getter
@Setter
public class CouponUpdateRequest {

  @EnumerationValidator(value = CouponType.class, nullable = true)
  private String type;

  @Size(min = 1, max = 64)
  private String title;

  @Size(max = 64)
  private String subtitle;

  @Size(max = 64)
  private String brandName;

  @Size(max = 1024)
  private String description;

  @Size(max = 32)
  private String color;

  @Valid private CouponDateInfo dateInfo;

  @Size(max = 2048)
  private String notice;

  @Pattern(regexp = "^1(3[0-9]|4[579]|5[0-35-9]|8[0-9]|7[0-9])\\d{8}$")
  private String servicePhone;

  private Boolean canShare;

  @Min(0)
  private Float value;

  @Min(1)
  private Integer getLimit;

  private Set<String> stores;

  @Valid private CouponExtension extension;
}
