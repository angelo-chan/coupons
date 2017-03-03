package com.chainz.coupon.shared.objects;

import com.chainz.coupon.shared.validator.common.EnumerationValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Coupon issuer.
 */
@Data
public class CouponIssuer {

  @NotNull
  @EnumerationValidator(enumClazz = CouponIssuerType.class)
  private CouponIssuerType issuerType;

  @Size(max = 128)
  private String issuerId;

}
