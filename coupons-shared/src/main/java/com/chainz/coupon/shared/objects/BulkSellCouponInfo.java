package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

/** Bulk sell coupon info. */
@Setter
@Getter
public class BulkSellCouponInfo {

  protected Long id;

  protected Integer sku;

  protected BulkCouponInfo coupon;
}
