package com.chainz.coupon.core.credentials;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/** Operator information. */
@Data
public class Operator {

  private String accountType;

  private String vendorId;

  private String openId;

  private Map<String, Object> context = new HashMap();

  public Operator() {}

  /**
   * Constructor.
   *
   * @param accountType account type.
   * @param vendorId vendor id.
   * @param openId open id.
   */
  public Operator(String accountType, String vendorId, String openId) {
    this.accountType = accountType;
    this.vendorId = vendorId;
    this.openId = openId;
  }

  /**
   * put key value to operator context.
   *
   * @param key key.
   * @param object value.
   */
  public void put(String key, Object object) {
    context.put(key, object);
  }

  /**
   * remove key.
   *
   * @param key key
   */
  public void remove(String key) {
    context.remove(key);
  }
}
