package com.chainz.coupon.shared.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Coupon date info validator. */
@SuppressWarnings({"JavadocMethod"})
@Documented
@Constraint(validatedBy = CouponDateInfoValidatorImpl.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface CouponDateInfoValidator {

  String message() default "Invalid coupon date info";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
