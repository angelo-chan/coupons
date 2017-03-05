package com.chainz.coupon.core.exception.common;

import com.chainz.coupon.core.exception.base.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;

/** General exception handler. */
@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

  @Autowired private MapperFacade mapperFacade;

  /**
   * handle application exception.
   *
   * @param exception application exception.
   * @param response http servlet response.
   * @return application error response.
   */
  @ExceptionHandler(ApplicationException.class)
  @ResponseBody
  public ApplicationErrorResponseEntity handleApplicationException(
      ApplicationException exception, HttpServletResponse response) {
    response.setStatus(exception.getHttpStatusCode());
    return mapperFacade.map(exception, ApplicationErrorResponseEntity.class);
  }

  /**
   * handle method argument not valid exception.
   *
   * @param exception method argument not valid exception.
   * @param response http servlet response.
   * @return validation error response.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ValidationErrorResponseEntity handleValidationException(
      MethodArgumentNotValidException exception, HttpServletResponse response) {
    response.setStatus(org.apache.http.HttpStatus.SC_UNPROCESSABLE_ENTITY);
    return new ValidationErrorResponseEntity()
        .fromObjectErrors(exception.getBindingResult().getAllErrors());
  }

  /** handle bad request. */
  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class
  })
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void handleBadRequest() {}
}
