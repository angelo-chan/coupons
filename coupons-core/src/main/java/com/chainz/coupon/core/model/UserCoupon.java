package com.chainz.coupon.core.model;

import com.chainz.coupon.shared.objects.CouponDateType;
import com.chainz.coupon.shared.objects.OutId;
import com.chainz.coupon.shared.objects.UserCouponStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * User coupon represent a coupon instance.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(
  name = "user_coupons",
  indexes = {
    @Index(columnList = "open_id"),
    @Index(columnList = "begin_date"),
    @Index(columnList = "end_date"),
    @Index(columnList = "coupon_code"),
    @Index(columnList = "consumed_at"),
    @Index(columnList = "open_id, end_date")
  }
)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class UserCoupon implements Serializable {

  private static final long serialVersionUID = -4661408922013589795L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id")
  private Coupon coupon;

  @Column(name = "open_id")
  private String openId;

  /**
   * will be used in future.
   */
  @Column(name = "user_id")
  private String userId;

  @Column(name = "coupon_code", nullable = false, unique = true)
  private String couponCode;

  @Column(name = "begin_date")
  private LocalDate beginDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "out_id")
  private OutId outId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private UserCouponStatus status = UserCouponStatus.UNUSED;

  /**
   * Indicate which store consumes the coupon.
   */
  @Column(name = "store_id")
  private String storeId;

  // to track share from user.
  @Column(name = "from_open_id")
  private String fromOpenId;

  @Column(name = "from_user_id")
  private String fromUserId;

  // the last user get the coupon at
  @Column(name = "got_at")
  private ZonedDateTime gotAt = ZonedDateTime.now();

  @Column(name = "consumed_at")
  private ZonedDateTime consumedAt;

  @CreatedDate
  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Version
  private Integer rev;

  /**
   * New a user coupon from coupon.
   *
   * @param coupon coupon
   * @return user coupon.
   */
  public static UserCoupon newFromCoupon(Coupon coupon) {
    UserCoupon userCoupon = new UserCoupon();
    userCoupon.setCoupon(coupon);
    CouponDateInfo dateInfo = coupon.getDateInfo();
    LocalDate start;
    LocalDate end;
    if (CouponDateType.DATE_TYPE_FIXED_TIME_RANGE == dateInfo.getDateType()) {
      start = dateInfo.getTimeRangeStart();
      end = dateInfo.getTimeRangeEnd();
    } else {
      LocalDate now = LocalDate.now();
      start = now.plusDays(dateInfo.getFixedBeginTerm());
      end = start.plusDays(dateInfo.getFixedTerm() - 1);
    }
    userCoupon.setBeginDate(start);
    userCoupon.setEndDate(end);
    return userCoupon;
  }
}
