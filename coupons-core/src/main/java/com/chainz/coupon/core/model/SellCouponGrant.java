package com.chainz.coupon.core.model;

import com.chainz.coupon.shared.objects.SellCouponGrantStatus;
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
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.ZonedDateTime;

/** Coupon model defines a coupon template which could be used to generate coupon instance. */
@Data
@Entity
@Table(
  name = "sell_coupon_grants",
  indexes = {
    @Index(columnList = "open_id"),
    @Index(columnList = "status"),
    @Index(columnList = "created_at"),
    @Index(columnList = "status, created_at")
  }
)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class SellCouponGrant {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "sell_coupon_id")
  private SellCoupon sellCoupon;

  @Column(name = "open_id", nullable = false)
  private String openId;

  @Column(name = "count", nullable = false)
  private Integer count;

  // the remain will not change after the grant expire
  @Column(name = "remain", nullable = false)
  private Integer remain;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private SellCouponGrantStatus status;

  @CreatedDate
  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Column(name = "expired_at")
  private ZonedDateTime expiredAt;

  @Version private Integer rev;

  /** Decrease remain and return the current remain. */
  public void decreaseRemain() {
    if (--remain == 0) {
      status = SellCouponGrantStatus.COMPLETED;
    }
  }
}
