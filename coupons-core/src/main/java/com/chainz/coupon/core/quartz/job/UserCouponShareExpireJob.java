package com.chainz.coupon.core.quartz.job;

import com.chainz.coupon.core.service.UserCouponShareService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/** Scanning user coupon share expire or not and restore the user coupon to sharer. */
@Slf4j
public class UserCouponShareExpireJob implements Job {

  @Autowired private UserCouponShareService userCouponShareService;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("begin to do user coupon share expire check scheduler");
    try {
      userCouponShareService.checkUserCouponShares();
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
    }
    log.info("end to do user coupon share expire check scheduler");
  }
}
