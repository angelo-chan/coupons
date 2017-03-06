package com.chainz.coupon.core.credentials;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Use to extract operator information. */
public class OperatorFilter extends OncePerRequestFilter {

  private static final String ACCOUNT_TYPE = "Account_Type";

  private static final String VENDOR_ID = "Vendor_Id";

  private static final String OPEN_ID = "Open_Id";

  private static final String ACCOUNT_ID = "Account_Id";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String accountType = request.getHeader(ACCOUNT_TYPE);
    if (StringUtils.isEmpty(accountType)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    String vendorId = request.getHeader(VENDOR_ID);
    String openId = request.getHeader(OPEN_ID);
    String accountId = request.getHeader(ACCOUNT_ID);
    Operator operator = new Operator(accountType.toUpperCase(), vendorId, openId, accountId);
    OperatorManager.setOperator(operator);
    filterChain.doFilter(request, response);
  }
}
