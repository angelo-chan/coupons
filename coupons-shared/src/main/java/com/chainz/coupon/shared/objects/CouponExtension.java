package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

/** Coupon extension. */
@Getter
@Setter
public class CouponExtension {

  @Size(max = 1024)
  private String customLinkName;

  @Size(max = 1024)
  private String customLinkTitle;

  @Size(max = 1024)
  private String customLinkUrl;
}
