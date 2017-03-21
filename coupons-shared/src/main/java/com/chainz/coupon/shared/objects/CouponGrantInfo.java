package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

/** Coupon grant info. */
@Getter
@Setter
public class CouponGrantInfo {

    protected String grantCode;

    protected Integer sku;

    protected BulkCouponInfo coupon;

}
