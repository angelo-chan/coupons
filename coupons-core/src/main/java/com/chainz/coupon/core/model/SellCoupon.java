package com.chainz.coupon.core.model;

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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.ZonedDateTime;

/** Coupon model defines a coupon template which could be used to generate coupon instance. */
@Data
@Entity
@Table(
  name = "sell_coupons",
  indexes = {
    @Index(columnList = "open_id"),
    @Index(columnList = "coupon_id"),
    @Index(columnList = "open_id,coupon_id")
  }
)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class SellCoupon implements Serializable {

  private static final long serialVersionUID = -4661408922013589795L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "coupon_id")
  private Coupon coupon;

  @Column(name = "open_id")
  private String openId;

  /** will be used in future. */
  @Column(name = "user_id")
  private String userId;

  @Column(name = "sku")
  private Integer sku;

  @CreatedDate
  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Version private Integer rev;
}
