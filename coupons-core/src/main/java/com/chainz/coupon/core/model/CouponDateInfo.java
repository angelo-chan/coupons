package com.chainz.coupon.core.model;

import com.chainz.coupon.shared.objects.CouponDateType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Coupon date type.
 */
@Data
@Embeddable
public class CouponDateInfo implements Serializable {

  private static final long serialVersionUID = 4706473442595061917L;

  @Enumerated(EnumType.STRING)
  @Column(name = "date_type", nullable = false)
  private CouponDateType dateType;

  @Column(name = "fixed_begin_term")
  private Integer fixedBeginTerm;

  @Column(name = "fixed_term")
  private Integer fixedTerm;

  @Column(name = "time_range_start")
  private LocalDate timeRangeStart;

  @Column(name = "time_range_end")
  private LocalDate timeRangeEnd;

}
