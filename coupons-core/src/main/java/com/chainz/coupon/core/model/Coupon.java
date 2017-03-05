package com.chainz.coupon.core.model;

import com.chainz.coupon.shared.objects.CouponDateType;
import com.chainz.coupon.shared.objects.CouponStatus;
import com.chainz.coupon.shared.objects.CouponTarget;
import com.chainz.coupon.shared.objects.CouponType;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

/** Coupon model defines a coupon template which could be used to generate coupon instance. */
@Data
@Entity
@Table(
  name = "coupons",
  indexes = {
    @Index(columnList = "target"),
    @Index(columnList = "status"),
    @Index(columnList = "issuer_type"),
    @Index(columnList = "issuer_id")
  }
)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Coupon implements Serializable {

  private static final long serialVersionUID = 5217994721126155917L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, updatable = false)
  private CouponType type;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "subtitle")
  private String subtitle;

  @Column(name = "brand_name")
  private String brandName;

  @Column(name = "description", length = 1024)
  private String description;

  @Column(name = "color")
  private String color;

  @Embedded private CouponDateInfo dateInfo;

  @Column(name = "notice", columnDefinition = "text")
  private String notice;

  @Column(name = "service_phone", length = 20)
  private String servicePhone;

  @Column(name = "can_share", columnDefinition = "boolean DEFAULT true")
  private Boolean canShare = true;

  @Column(name = "circulation", nullable = false)
  private Long circulation;

  @Column(name = "sku", nullable = false)
  private Long sku;

  @Column(name = "value")
  private Float value;

  @Column(name = "get_limit", nullable = false)
  private Integer getLimit;

  @Enumerated(EnumType.STRING)
  @Column(name = "target", nullable = false)
  private CouponTarget target;

  @ElementCollection
  @CollectionTable(name = "stores", joinColumns = @JoinColumn(name = "coupon_id"))
  @OptimisticLock(excluded = true)
  @Column(name = "store")
  private Set<String> stores;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private CouponStatus status = CouponStatus.UNVERIFIED;

  @Embedded private CouponExtension extension;

  @Embedded private CouponIssuer issuer;

  @CreatedDate
  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Version private Integer rev;

  /** pre save to sanity the date info. */
  @PrePersist
  @PreUpdate
  public void preSave() {
    if (CouponDateType.DATE_TYPE_FIXED_TERM == dateInfo.getDateType()) {
      dateInfo.setTimeRangeStart(null);
      dateInfo.setTimeRangeEnd(null);
    } else {
      dateInfo.setFixedTerm(null);
      dateInfo.setFixedBeginTerm(null);
    }
    if (CouponTarget.STORE != target) {
      stores = null;
    }
  }
}
