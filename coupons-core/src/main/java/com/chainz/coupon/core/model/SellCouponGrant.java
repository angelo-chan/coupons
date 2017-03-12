package com.chainz.coupon.core.model;

import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.shared.objects.SellCouponGrantStatus;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLock;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Coupon model defines a coupon template which could be used to generate coupon instance. */
@Data
@Entity
@Table(
  name = "sell_coupon_grants",
  indexes = {@Index(columnList = "open_id"), @Index(columnList = "created_at")}
)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class SellCouponGrant implements Serializable {

  private static final long serialVersionUID = -3038221512328818034L;

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

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private SellCouponGrantStatus status = SellCouponGrantStatus.INPROGRESS;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "sell_coupon_grant_id")
  @OptimisticLock(
    excluded = true
  ) // excluding user coupon share entry from triggering version increment
  private List<SellCouponGrantEntry> sellCouponGrantEntries = new ArrayList<>();

  @CreatedDate
  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Column(name = "expired_at")
  private ZonedDateTime expiredAt;

  @Version private Integer rev;

  /**
   * Add sell coupon grant entry.
   *
   * @param sellCouponGrantEntry user coupon share entry.
   */
  public void addSellCouponGrantEntry(SellCouponGrantEntry sellCouponGrantEntry) {
    sellCouponGrantEntries.add(sellCouponGrantEntry);
  }

  /**
   * new a sell coupon grant instance.
   *
   * @param openId open id.
   * @param count count.
   * @param sellCoupon sell coupon.
   * @return sell coupon grant.
   */
  public static SellCouponGrant newInstance(String openId, Integer count, SellCoupon sellCoupon) {
    SellCouponGrant sellCouponGrant = new SellCouponGrant();
    String uuid = UUID.randomUUID().toString();
    sellCouponGrant.setId(uuid);
    sellCouponGrant.setOpenId(openId);
    sellCouponGrant.setCount(count);
    sellCouponGrant.setExpiredAt(
        ZonedDateTime.now().plusSeconds(Constants.SELL_COUPON_GRANT_TIMEOUT));
    sellCouponGrant.setSellCoupon(sellCoupon);
    return sellCouponGrant;
  }
}
