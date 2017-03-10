package com.chainz.coupon.core.credentials;

import com.chainz.coupon.core.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;

/** Operator information. */
@Data
@AllArgsConstructor
public class Operator {

  private String accountType;

  private String vendorId;

  private String storeId;

  private String openId;

  public boolean isClient() {
    return Constants.CLIENT.equals(accountType);
  }

  public boolean isSystem() {
    return Constants.SYSTEM.equals(accountType);
  }

  public boolean isVendor() {
    return Constants.VENDOR.equals(accountType);
  }

  public boolean isStore() {
    return Constants.STORE.equals(accountType);
  }
}
