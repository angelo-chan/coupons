package com.chainz.coupon.core.quartz.job;

import com.chainz.coupon.core.service.SellCouponGrantService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/** Scanning sell coupon grant expire or not and restore the sell coupon to seller. */
@Slf4j
public class SellCouponGrantExpireJob implements Job {

  @Autowired private SellCouponGrantService sellCouponGrantService;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("begin to do sell coupon grant expire check scheduler");
    try {
      sellCouponGrantService.checkSellCouponGrants();
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
    }
    log.info("end to do sell coupon grant expire check scheduler");
  }
}
