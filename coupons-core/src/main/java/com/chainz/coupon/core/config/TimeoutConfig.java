package com.chainz.coupon.core.config;

import com.chainz.coupon.core.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** Configuration. */
@Configuration
@ConfigurationProperties("coupons.timeout")
@Setter
@Getter
public class TimeoutConfig {

  private int couponGrant = Constants.COUPON_GRANT_TIMEOUT;

  private int sellCouponGrant = Constants.SELL_COUPON_GRANT_TIMEOUT;

  private int userCouponShare = Constants.USER_COUPON_SHARE_TIMEOUT;
}
