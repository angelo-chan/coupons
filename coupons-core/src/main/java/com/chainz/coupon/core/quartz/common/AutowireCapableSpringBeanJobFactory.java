package com.chainz.coupon.core.quartz.common;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/** Adds autowiring support to quartz jobs. */
public final class AutowireCapableSpringBeanJobFactory extends SpringBeanJobFactory
    implements ApplicationContextAware {

  private transient AutowireCapableBeanFactory beanFactory;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    beanFactory = applicationContext.getAutowireCapableBeanFactory();
  }

  @Override
  protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
    Object job = super.createJobInstance(bundle);
    assert beanFactory != null;
    beanFactory.autowireBean(job);
    return job;
  }
}
