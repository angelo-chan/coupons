package com.chainz.coupon.core.model;

import com.chainz.coupon.shared.objects.CouponIssuerType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/** Coupon issuer. */
@Data
@Embeddable
public class CouponIssuer implements Serializable {

  private static final long serialVersionUID = -4440517847424655497L;

  @Enumerated(EnumType.STRING)
  @Column(name = "issuer_type", nullable = false)
  private CouponIssuerType issuerType;

  @Column(name = "issuer_id")
  private String issuerId;
}
