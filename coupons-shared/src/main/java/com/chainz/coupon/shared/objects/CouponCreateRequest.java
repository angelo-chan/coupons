package com.chainz.coupon.shared.objects;

import com.chainz.coupon.shared.validator.common.EnumerationValidator;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * coupon create request.
 */
@Data
public class CouponCreateRequest {

  @NotNull
  @EnumerationValidator(enumClazz = CouponType.class)
  private String type;

  @NotNull
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

  @Valid
  private CouponDateInfo dateInfo;

  @Size(max = 2048)
  private String notice;

  @Pattern(regexp = "^1(3[0-9]|4[579]|5[0-35-9]|8[0-9]|7[0-9])\\d{8}$")
  private String servicePhone;

  private Boolean canShare = true;

  @NotNull
  @Min(1)
  private Integer circulation;

  @NotNull
  @Min(0)
  private Float value;

  @NotNull
  @Min(1)
  private Integer getLimit;

  @NotNull
  @EnumerationValidator(enumClazz = CouponTarget.class)
  private String target;

  private Set<String> stores;

  @Valid
  private CouponExtension extension;

  @Valid
  private CouponIssuer issuer;

}
