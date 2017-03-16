package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.config.SchedulerConfig;
import com.chainz.coupon.core.config.TimeoutConfig;
import com.chainz.coupon.core.credentials.ClientPermission;
import com.chainz.coupon.core.credentials.Operator;
import com.chainz.coupon.core.credentials.OperatorManager;
import com.chainz.coupon.core.exception.UserCouponShareNotFoundException;
import com.chainz.coupon.core.exception.UserCouponShareStatusConflictException;
import com.chainz.coupon.core.model.QUserCoupon;
import com.chainz.coupon.core.model.QUserCouponShare;
import com.chainz.coupon.core.model.UserCouponShare;
import com.chainz.coupon.core.model.UserCouponShareEntry;
import com.chainz.coupon.core.repository.UserCouponShareRepository;
import com.chainz.coupon.core.repository.common.JoinDescriptor;
import com.chainz.coupon.core.service.UserCouponShareService;
import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.shared.objects.UserCouponShareInfo;
import com.chainz.coupon.shared.objects.UserCouponShareStatus;
import com.chainz.coupon.shared.objects.UserCouponStatus;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/** User coupon share service implementation. */
@Slf4j
@Service
public class UserCouponShareServiceImpl implements UserCouponShareService {

  @Autowired private UserCouponShareRepository userCouponShareRepository;
  @Autowired private MapperFacade mapperFacade;
  @Autowired private StringRedisTemplate stringRedisTemplate;
  @PersistenceContext private EntityManager entityManager;
  @Autowired private TimeoutConfig timeoutConfig;
  @Autowired private SchedulerConfig schedulerConfig;

  @Override
  @ClientPermission
  @Transactional(readOnly = true)
  public UserCouponShareInfo getUserCouponShare(String shareCode)
      throws UserCouponShareNotFoundException {
    // use predicate and join to avoid lazy load
    QUserCouponShare qUserCouponShare = QUserCouponShare.userCouponShare;
    UserCouponShare userCouponShare =
        userCouponShareRepository.findOne(
            qUserCouponShare.id.eq(shareCode), JoinDescriptor.join(qUserCouponShare.coupon));
    if (userCouponShare == null) {
      throw new UserCouponShareNotFoundException(shareCode);
    }
    UserCouponShareInfo userCouponShareInfo =
        mapperFacade.map(userCouponShare, UserCouponShareInfo.class);
    if (userCouponShareInfo.getStatus() == UserCouponShareStatus.INPROGRESS) {
      String key = Constants.USER_COUPON_SHARE_PREFIX + shareCode;
      Long remain = stringRedisTemplate.opsForList().size(key);
      userCouponShareInfo.setRemain(remain.intValue());
    }
    return userCouponShareInfo;
  }

  @Override
  @ClientPermission
  @Transactional
  public void abortUserCouponShare(String shareCode)
      throws UserCouponShareNotFoundException, UserCouponShareStatusConflictException {
    Operator operator = OperatorManager.getOperator();
    String openId = operator.getOpenId();
    QUserCouponShare qUserCouponShare = QUserCouponShare.userCouponShare;
    Predicate predicate = qUserCouponShare.id.eq(shareCode).and(qUserCouponShare.openId.eq(openId));
    UserCouponShare userCouponShare = userCouponShareRepository.findOne(predicate);
    if (userCouponShare == null) {
      throw new UserCouponShareNotFoundException(shareCode);
    }
    if (userCouponShare.getStatus() != UserCouponShareStatus.INPROGRESS) {
      throw new UserCouponShareStatusConflictException(
          userCouponShare.getId(), userCouponShare.getStatus());
    }
    cancelUserCouponShare(userCouponShare, UserCouponShareStatus.ABORTED);
    String key = Constants.USER_COUPON_SHARE_PREFIX + shareCode;
    stringRedisTemplate.delete(key);
  }

  @Override
  @Transactional
  public void checkUserCouponShares() {
    QUserCouponShare qUserCouponShare = QUserCouponShare.userCouponShare;
    ZonedDateTime now = ZonedDateTime.now();
    int userCouponShareTimeout = timeoutConfig.getUserCouponShare();
    long threshold = schedulerConfig.getThreshold().getUserCouponShare();
    ZonedDateTime end = now.minus(userCouponShareTimeout, ChronoUnit.SECONDS);
    ZonedDateTime start = end.minus(threshold, ChronoUnit.SECONDS);

    Predicate predicate =
        qUserCouponShare
            .createdAt
            .after(start)
            .and(qUserCouponShare.createdAt.before(end))
            .and(qUserCouponShare.status.eq(UserCouponShareStatus.INPROGRESS));
    JPAQuery<Void> query = new JPAQuery<>(entityManager);
    List<String> ids =
        query.from(qUserCouponShare).select(qUserCouponShare.id).where(predicate).fetch();
    log.info("found [{}] expired user coupon shares.", ids.size());
    for (String id : ids) {
      try {
        expireUserCouponShare(id);
      } catch (RuntimeException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  /**
   * Expire user coupon share.
   *
   * @param id user coupon share id.
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void expireUserCouponShare(String id) {
    log.info("begin to expire user coupon share [{}]", id);
    QUserCouponShare qUserCouponShare = QUserCouponShare.userCouponShare;
    Predicate predicate =
        qUserCouponShare
            .id
            .eq(id)
            .and(qUserCouponShare.status.eq(UserCouponShareStatus.INPROGRESS));
    UserCouponShare userCouponShare = userCouponShareRepository.findOne(predicate);
    if (userCouponShare == null) {
      return;
    }
    cancelUserCouponShare(userCouponShare, UserCouponShareStatus.EXPIRED);
    log.info("success to expire user coupon share [{}]", id);
  }

  /**
   * cancel user coupon share.
   *
   * @param userCouponShare user coupon share.
   * @param destStatus destination status.
   */
  private void cancelUserCouponShare(
      UserCouponShare userCouponShare, UserCouponShareStatus destStatus) {
    log.debug("begin to reset user coupon share: {}", userCouponShare.getId());
    List<Long> returnedUserCoupons =
        userCouponShare
            .getUserCouponShareEntries()
            .values()
            .stream()
            .filter(entry -> entry.isGot() == false)
            .map(UserCouponShareEntry::getUserCouponId)
            .collect(Collectors.toList());
    log.debug(
        "found remain count: {} of {} for user coupon share: {}",
        returnedUserCoupons.size(),
        userCouponShare.getCount(),
        userCouponShare.getId());
    if (returnedUserCoupons.size() > 0) {
      QUserCoupon userCoupon = QUserCoupon.userCoupon;
      new JPAUpdateClause(entityManager, userCoupon)
          .where(userCoupon.id.in(returnedUserCoupons))
          .set(userCoupon.status, UserCouponStatus.UNUSED)
          .execute();
    }
    userCouponShare.setStatus(destStatus);
    userCouponShareRepository.save(userCouponShare);
    log.debug(
        "success to reset user coupon share: {}, and return {} user coupon",
        userCouponShare.getId(),
        returnedUserCoupons.size());
  }
}
