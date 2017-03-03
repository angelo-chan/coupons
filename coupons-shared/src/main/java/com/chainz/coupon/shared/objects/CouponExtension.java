package com.chainz.coupon.shared.objects;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Coupon extension.
 */
@Data
public class CouponExtension {

  @Size(max = 1024)
  private String customLinkName;

  @Size(max = 1024)
  private String customLinkTitle;

  @Size(max = 1024)
  private String customLinkUrl;

}
