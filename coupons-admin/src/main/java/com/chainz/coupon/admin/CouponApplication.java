package com.chainz.coupon.admin;

import com.chainz.coupon.core.credentials.OperatorFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

import javax.servlet.Filter;
import java.util.TimeZone;

/** Coupon admin application. */
@ComponentScan(value = "com.chainz")
@EntityScan(
  value = "com.chainz.coupon",
  basePackageClasses = {Jsr310JpaConverters.class, CouponApplication.class}
)
@EnableJpaRepositories(value = "com.chainz.coupon")
@EnableCaching
@EnableRetry
@EnableJpaAuditing
@SpringBootApplication
public class CouponApplication {
  /**
   * main entry for coupon admin application.
   *
   * @param args application args.
   */
  public static void main(String[] args) {
    // manually set default timezone
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    SpringApplication.run(CouponApplication.class, args);
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  Filter operatorFilter() {
    return new OperatorFilter();
  }
}
