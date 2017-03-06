package com.chainz.coupon.core.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/** Operator information. */
@Data
@AllArgsConstructor
public class Operator implements Serializable {

  private static final long serialVersionUID = -1730821500922712409L;

  private String accountType;

  private String vendorId;

  private String openId;

  private String accountId;
}
