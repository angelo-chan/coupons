package com.chainz.coupon.core.validator.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/** Method validate config. */
@Configuration
public class MethodValidationConfig {

  /**
   * method validation on request or path param.
   *
   * @return MethodValidationPostProcessor.
   */
  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    return new MethodValidationPostProcessor();
  }
}
