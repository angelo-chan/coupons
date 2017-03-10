package com.chainz.coupon.core.credentials;

import com.chainz.coupon.core.utils.Constants;
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

  private static final String STORE_ID = "Store_Id";

  private static final String OPEN_ID = "Open_Id";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String accountType = request.getHeader(ACCOUNT_TYPE);
    if (StringUtils.isEmpty(accountType)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    accountType = accountType.toUpperCase();
    String vendorId = request.getHeader(VENDOR_ID);
    if ((Constants.VENDOR.equals(accountType) || Constants.STORE.equals(accountType))
        && StringUtils.isEmpty(vendorId)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    String storeId = request.getHeader(STORE_ID);
    if (Constants.STORE.equals(accountType) && StringUtils.isEmpty(storeId)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    String openId = request.getHeader(OPEN_ID);
    if (Constants.CLIENT.equals(accountType) && StringUtils.isEmpty(openId)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    Operator operator = new Operator(accountType, vendorId, storeId, openId);
    OperatorManager.setOperator(operator);
    filterChain.doFilter(request, response);
  }
}
