package com.chainz.coupon.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * User coupon share entry.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "user_coupon_share_entries")
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class UserCouponShareEntry implements Serializable {

  private static final long serialVersionUID = -3412098430684168692L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "user_coupon_id", nullable = false)
  private Long userCouponId;

  @Column(name = "got")
  private boolean got = false;

  // indicate who get the user coupon
  @Column(name = "open_id")
  private String openId;

  // or this is not a wechat user.
  @Column(name = "user_id")
  private String userId;

  // indicate when get the user coupon
  @Column(name = "got_at")
  private ZonedDateTime gotAt;

  @CreatedDate
  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  /**
   * Default constructor.
   */
  public UserCouponShareEntry() {
  }

  /**
   * Constructor.
   *
   * @param userCouponId user coupon id.
   */
  public UserCouponShareEntry(Long userCouponId) {
    this.userCouponId = userCouponId;
  }

  @Version
  private Integer rev;
}
