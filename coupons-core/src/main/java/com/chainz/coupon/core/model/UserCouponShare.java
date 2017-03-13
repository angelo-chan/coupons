package com.chainz.coupon.core.model;

import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.shared.objects.UserCouponShareStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * user coupon shares.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(
  name = "user_coupon_shares",
  indexes = {@Index(columnList = "open_id"), @Index(columnList = "created_at")}
)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class UserCouponShare implements Serializable {

  private static final long serialVersionUID = 8239762727029852641L;

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id")
  private Coupon coupon;

  // indicate who did the share
  @Column(name = "open_id")
  private String openId;

  // or this is not a wechat user
  @Column(name = "user_id")
  private String userId;

  // this is a redundant key since userCouponShareEntries
  @Column(name = "count", nullable = false)
  private Integer count;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_coupon_share_id")
  @MapKey(name = "userCouponId")
  @OptimisticLock(
    excluded = true
  ) // excluding user coupon share entry from triggering version increment
  private Map<Long, UserCouponShareEntry> userCouponShareEntries = new HashMap<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private UserCouponShareStatus status = UserCouponShareStatus.INPROGRESS;

  @CreatedDate
  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Column(name = "expired_at")
  private ZonedDateTime expiredAt;

  @Version
  private Integer rev;

  /**
   * Add user coupon share entry.
   *
   * @param userCouponShareEntry user coupon share entry.
   */
  public void addUserCouponShareEntry(UserCouponShareEntry userCouponShareEntry) {
    userCouponShareEntries.put(userCouponShareEntry.getUserCouponId(), userCouponShareEntry);
  }

  /**
   * new user coupon share instance.
   *
   * @param coupon           coupon.
   * @param userCouponIdList user coupon id list.
   * @return a new user coupon share instance.
   */
  public static UserCouponShare newInstance(Coupon coupon, List<Long> userCouponIdList) {
    final UserCouponShare userCouponShare = new UserCouponShare();
    String uuid = UUID.randomUUID().toString();
    userCouponShare.setId(uuid);
    Integer size = userCouponIdList.size();
    userCouponShare.setCount(size);
    userCouponShare.setExpiredAt(
      ZonedDateTime.now().plusSeconds(Constants.USER_COUPON_SHARE_TIMEOUT));
    userCouponShare.setCoupon(coupon);
    userCouponIdList
      .stream()
      .map(UserCouponShareEntry::new)
      .forEach(userCouponShare::addUserCouponShareEntry);
    return userCouponShare;
  }
}
