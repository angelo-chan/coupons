package com.chainz.coupon.core.orika.common;

import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;

/**
 * Java 8 time orika pass through converter.
 */
@Component
public class JavaTimePassThroughConverterFactoryBean extends AbstractFactoryBean<PassThroughConverter> {

  @Override
  public Class<?> getObjectType() {
    return PassThroughConverter.class;
  }

  @Override
  protected PassThroughConverter createInstance() throws Exception {
    return new PassThroughConverter(ZonedDateTime.class, LocalDateTime.class,
      LocalDate.class, Instant.class, Period.class);
  }
}
