package com.chainz.coupon.core.model;

import com.chainz.coupon.shared.objects.CouponTarget;
import com.chainz.coupon.shared.objects.CouponType;
import com.chainz.coupon.shared.objects.UserCouponStatus;
import lombok.Data;
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

/** User coupon represent a coupon instance. */
@Data
//@Entity
@Table(
  name = "user_coupons",
  indexes = {
    @Index(columnList = "open_id"),
    @Index(columnList = "user_id"),
    @Index(columnList = "status"),
    @Index(columnList = "open_id, status"),
    @Index(columnList = "user_id, status"),
    @Index(columnList = "coupon_code"),
    @Index(columnList = "begin, end"),
    @Index(columnList = "out_id"),
    @Index(columnList = "original_open_id")
  }
)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class UserCoupon implements Serializable {
  private static final long serialVersionUID = -4661408922013589795L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "coupon_id")
  private Coupon coupon;

  @Column(name = "open_id")
  private String openId;

  /** will be used in future. */
  @Column(name = "user_id")
  private String userId;

  @Column(name = "coupon_code", nullable = false)
  private String couponCode;

  @Column(name = "begin")
  private LocalDate begin;

  @Column(name = "end")
  private LocalDate end;

  @Column(name = "out_id")
  private String outId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private UserCouponStatus status = UserCouponStatus.UNUSED;

  // and below are the redundant information from coupon begin
  private CouponType type;

  private String title;

  private String subtitle;

  private String brandName;

  private String color;

  private Float value;

  private CouponTarget target;

  private Boolean canShare = true;

  // redundant information end

  /** to track share from seller. */
  @Column(name = "original_open_id")
  private String originalOpenId;

  @CreatedDate
  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Version private Integer rev;
}
