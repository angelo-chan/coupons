package com.chainz.coupon.core.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Operator information. */
@Data
@AllArgsConstructor
public class Operator {

  private String accountType;

  private String vendorId;

  private String openId;
}
