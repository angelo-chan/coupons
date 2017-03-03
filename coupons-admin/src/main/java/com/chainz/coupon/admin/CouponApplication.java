package com.chainz.coupon.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Coupon admin application.
 */
@ComponentScan(value = "com.chainz")
@EntityScan(value = "com.chainz.coupon", basePackageClasses = {Jsr310JpaConverters.class, CouponApplication.class})
@EnableJpaRepositories(value = "com.chainz.coupon")
@EnableCaching
@EnableRetry
@EnableJpaAuditing
@SpringBootApplication
public class CouponApplication {
  public static void main(String[] args) {
    SpringApplication.run(CouponApplication.class, args);
  }
}
