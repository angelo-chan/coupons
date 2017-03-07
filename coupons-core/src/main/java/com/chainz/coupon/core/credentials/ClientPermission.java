package com.chainz.coupon.core.credentials;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Client permission annotation. */
@Target({METHOD})
@Retention(RUNTIME)
public @interface ClientPermission {}
