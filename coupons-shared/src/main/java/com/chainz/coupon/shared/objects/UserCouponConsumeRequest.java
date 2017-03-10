package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/** User coupon consume request. */
@Getter
@Setter
public class UserCouponConsumeRequest {

  @NotNull
  @Min(1)
  private String storeId;

  @NotNull
  @Size(min = 1, max = 30)
  private List<Long> ids;
}
