package com.chainz.coupon.core.quartz.job;

import com.chainz.coupon.core.config.SchedulerConfig;
import com.chainz.coupon.core.quartz.common.QuartzHelper;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/** Trigger configuration for coupons. */
@Configuration
public class TriggerConfig {

  /**
   * create sell coupon grant expire job.
   *
   * @return job detail.
   */
  @Bean(name = "sellCouponGrantExpireJobDetail")
  public JobDetailFactoryBean sellCouponGrantExpireJobDetail() {
    return QuartzHelper.jobDetailFactoryBean(SellCouponGrantExpireJob.class);
  }

  /**
   * create sell coupon grant expire simple trigger.
   *
   * @param jobDetail job detail.
   * @param schedulerConfig scheduler config.
   * @return simple trigger.
   */
  @Bean(name = "sellCouponGrantExpireSimpleTrigger")
  public SimpleTriggerFactoryBean sellCouponGrantExpireSimpleTrigger(
      @Qualifier("sellCouponGrantExpireJobDetail") JobDetail jobDetail,
      SchedulerConfig schedulerConfig) {
    return QuartzHelper.simpleTriggerFactoryBean(
        jobDetail,
        schedulerConfig.getDelay().getSellCouponGrant() * 1000,
        schedulerConfig.getInterval().getSellCouponGrant() * 1000);
  }

  /**
   * create user coupon share expire job detail.
   *
   * @return job detail.
   */
  @Bean(name = "userCouponShareExpireJobDetail")
  public JobDetailFactoryBean userCouponShareExpireJobDetail() {
    return QuartzHelper.jobDetailFactoryBean(UserCouponShareExpireJob.class);
  }

  /**
   * create user coupon share expire simple trigger.
   *
   * @param jobDetail job detail.
   * @param schedulerConfig scheduler config.
   * @return simple trigger.
   */
  @Bean(name = "userCouponShareExpireSimpleTrigger")
  public SimpleTriggerFactoryBean userCouponShareExpireSimpleTrigger(
      @Qualifier("userCouponShareExpireJobDetail") JobDetail jobDetail,
      SchedulerConfig schedulerConfig) {
    return QuartzHelper.simpleTriggerFactoryBean(
        jobDetail,
        schedulerConfig.getDelay().getUserCouponShare() * 1000,
        schedulerConfig.getInterval().getUserCouponShare() * 1000);
  }
}
