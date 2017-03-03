package com.chainz.coupon.core.exception.base;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.helpers.MessageFormatter;

/**
 * Base class for chainz exception system.
 */
@Getter
@Setter
public abstract class BaseException extends RuntimeException {

  protected String message;

  protected String messageTemplate;


  protected BaseException(Exception e) {
    super(e);
    this.message = e.getMessage();
  }

  protected BaseException(String pattern, Object... args) {
    super();
    setMessageTemplate(pattern);
    setMessage(MessageFormatter.arrayFormat(pattern, args).getMessage());
  }

}
