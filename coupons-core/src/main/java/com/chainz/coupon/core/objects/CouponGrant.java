package com.chainz.coupon.core.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** Coupon grant. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponGrant implements Serializable {

  private static final long serialVersionUID = -710193500631386565L;

  private Long couponId;

  private Integer count;
}
