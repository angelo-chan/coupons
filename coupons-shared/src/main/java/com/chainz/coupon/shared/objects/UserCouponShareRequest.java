package com.chainz.coupon.shared.objects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/** User coupon consume request. */
@Getter
@Setter
public class UserCouponShareRequest {

  @NotNull
  private Long couponId;

  @NotNull
  @Size(min = 1, max = 30)
  private List<Long> ids;
}
