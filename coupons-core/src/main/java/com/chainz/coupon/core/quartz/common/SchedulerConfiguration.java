package com.chainz.coupon.core.quartz.common;

import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/** Scheduler configuration. */
@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class SchedulerConfiguration {

  /**
   * initialize job factory.
   *
   * @param applicationContext application context.
   * @return job factory.
   */
  @Bean
  public JobFactory jobFactory(ApplicationContext applicationContext) {
    AutowireCapableSpringBeanJobFactory jobFactory = new AutowireCapableSpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

  /**
   * return quartz properties.
   *
   * @return quartz properties.
   * @throws IOException io exception.
   */
  public Properties quartzProperties() throws IOException {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
    propertiesFactoryBean.afterPropertiesSet();
    return propertiesFactoryBean.getObject();
  }

  /**
   * initialize quartz scheduler factory bean.
   *
   * @param dataSource datasource.
   * @param jobFactory job factory.
   * @param triggers quartz triggers.
   * @return quartz factory bean.
   * @throws IOException io exception.
   */
  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(
      DataSource dataSource, JobFactory jobFactory, List<Trigger> triggers) throws IOException {
    SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
    // this allows to update triggers in DB while job setting change
    factoryBean.setOverwriteExistingJobs(true);
    factoryBean.setDataSource(dataSource);
    factoryBean.setJobFactory(jobFactory);
    factoryBean.setQuartzProperties(quartzProperties());
    factoryBean.setTriggers(triggers.toArray(new Trigger[triggers.size()]));
    return factoryBean;
  }
}
