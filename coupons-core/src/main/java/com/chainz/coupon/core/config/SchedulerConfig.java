package com.chainz.coupon.core.config;

import com.chainz.coupon.core.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** Configuration. */
@Configuration
@ConfigurationProperties("quartz.scheduler")
@Setter
@Getter
public class SchedulerConfig {

  private Interval interval;

  private Delay delay;

  private Threshold threshold;

  @Getter
  @Setter
  public static class Interval {
    private long sellCouponGrant = Constants.SELL_COUPON_GRANT_SCHEDULER_INTERVAL;
    private long userCouponShare = Constants.USER_COUPON_SHARE_SCHEDULER_INTERVAL;
  }

  @Getter
  @Setter
  public static class Delay {
    private long sellCouponGrant = Constants.SELL_COUPON_GRANT_SCHEDULER_DELAY;
    private long userCouponShare = Constants.USER_COUPON_SHARE_SCHEDULER_DELAY;
  }

  @Getter
  @Setter
  public static class Threshold {
    private long sellCouponGrant = Constants.SELL_COUPON_GRANT_SCHEDULER_THRESHOLD;
    private long userCouponShare = Constants.USER_COUPON_SHARE_SCHEDULER_THRESHOLD;
  }
}
