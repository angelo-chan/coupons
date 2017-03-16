package com.chainz.coupon.core.quartz.common;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/** Helper class for quartz create trigger and job detail. */
public class QuartzHelper {

  /**
   * create job detail factory bean.
   *
   * @param jobClass job class.
   * @return job detail factory bean.
   */
  public static JobDetailFactoryBean jobDetailFactoryBean(Class jobClass) {
    JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
    jobDetailFactoryBean.setJobClass(jobClass);
    // job has to be durable to db
    jobDetailFactoryBean.setDurability(true);
    return jobDetailFactoryBean;
  }

  /**
   * Create simple trigger factory bean.
   *
   * @param jobDetail job detail.
   * @param intervalInMs interval in ms.
   * @param delayInMs delay in ms.
   * @return simple trigger factory bean.
   */
  public static SimpleTriggerFactoryBean simpleTriggerFactoryBean(
      JobDetail jobDetail, long delayInMs, long intervalInMs) {
    SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
    simpleTriggerFactoryBean.setJobDetail(jobDetail);
    simpleTriggerFactoryBean.setStartDelay(delayInMs);
    simpleTriggerFactoryBean.setRepeatInterval(intervalInMs);
    simpleTriggerFactoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    simpleTriggerFactoryBean.setMisfireInstruction(
        SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return simpleTriggerFactoryBean;
  }

  /**
   * Create cron trigger factory bean.
   *
   * @param jobDetail job detail.
   * @param cronExpression cron expression.
   * @return cron trigger factory bean.
   */
  public static CronTriggerFactoryBean cronTriggerFactoryBean(
      JobDetail jobDetail, String cronExpression) {
    CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
    cronTriggerFactoryBean.setJobDetail(jobDetail);
    cronTriggerFactoryBean.setCronExpression(cronExpression);
    cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
    return cronTriggerFactoryBean;
  }
}
