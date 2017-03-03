package com.chainz.coupon.shared.validator.common;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enumeration validator.
 */
@SuppressWarnings({"JavadocMethod"})
@Documented
@Constraint(validatedBy = EnumerationValidatorImpl.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface EnumerationValidator {

  Class<? extends Enum<?>> enumClazz();

  String message() default "Enum value is not valid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
