package com.chainz.coupon.core.exception.common;

import com.chainz.coupon.core.exception.base.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * General exception handler.
 */
@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

  @Autowired
  private MapperFacade mapperFacade;

  @ExceptionHandler(ApplicationException.class)
  @ResponseBody
  public ErrorResponseEntity handleApplicationException(ApplicationException exception, HttpServletResponse response) {
    response.setStatus(exception.getHttpStatusCode());
    return mapperFacade.map(exception, ErrorResponseEntity.class);
  }

}
