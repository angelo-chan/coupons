package com.chainz.coupon.core.orika.common;

import ma.glasnost.orika.MapperFactory;

/**
 * Interface for orika mapper configure.
 */
public interface MapperConfigurer {

  void configure(MapperFactory mapperFactory);
}
