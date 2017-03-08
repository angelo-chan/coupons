package com.chainz.coupon.shared.objects;

import com.chainz.coupon.shared.validator.common.EnumerationValidator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** Coupon issuer. */
@Getter
@Setter
public class CouponIssuer {

  @NotNull
  @EnumerationValidator(CouponIssuerType.class)
  private String issuerType;

  @Size(max = 128)
  private String issuerId;
}
