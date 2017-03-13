package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.credentials.ClientPermission;
import com.chainz.coupon.core.credentials.Operator;
import com.chainz.coupon.core.credentials.OperatorManager;
import com.chainz.coupon.core.exception.UserCouponShareNotFoundException;
import com.chainz.coupon.core.model.QUserCoupon;
import com.chainz.coupon.core.model.QUserCouponShare;
import com.chainz.coupon.core.model.UserCouponShare;
import com.chainz.coupon.core.repository.UserCouponShareRepository;
import com.chainz.coupon.core.repository.common.JoinDescriptor;
import com.chainz.coupon.core.service.UserCouponShareService;
import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.shared.objects.UserCouponShareInfo;
import com.chainz.coupon.shared.objects.UserCouponShareStatus;
import com.chainz.coupon.shared.objects.UserCouponStatus;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
  public void abortUserCouponShare(String shareCode) throws UserCouponShareNotFoundException {
    Operator operator = OperatorManager.getOperator();
    String openId = operator.getOpenId();
    QUserCouponShare qUserCouponShare = QUserCouponShare.userCouponShare;
    Predicate predicate =
        qUserCouponShare
            .id
            .eq(shareCode)
            .and(qUserCouponShare.openId.eq(openId))
            .and(qUserCouponShare.status.eq(UserCouponShareStatus.INPROGRESS));
    UserCouponShare userCouponShare = userCouponShareRepository.findOne(predicate);
    if (userCouponShare == null) {
      throw new UserCouponShareNotFoundException(shareCode);
    }
    log.debug("begin to abort user coupon share: {}", shareCode);

    List<Long> returnedUserCoupons =
        userCouponShare
            .getUserCouponShareEntries()
            .values()
            .stream()
            .filter(entry -> entry.isGot() == false)
            .map(entry -> entry.getUserCouponId())
            .collect(Collectors.toList());
    log.debug(
        "found remain count: {} of {} for user coupon share: {}",
        returnedUserCoupons.size(),
        userCouponShare.getCount(),
        shareCode);
    if (returnedUserCoupons.size() > 0) {
      QUserCoupon userCoupon = QUserCoupon.userCoupon;
      new JPAUpdateClause(entityManager, userCoupon)
          .where(userCoupon.id.in(returnedUserCoupons))
          .set(userCoupon.status, UserCouponStatus.UNUSED)
          .execute();
    }
    userCouponShare.setStatus(UserCouponShareStatus.ABORTED);
    userCouponShareRepository.save(userCouponShare);
    String key = Constants.USER_COUPON_SHARE_PREFIX + shareCode;
    stringRedisTemplate.delete(key);
    log.debug(
        "success to abort user coupon share: {}, and return {} user coupon",
        shareCode,
        returnedUserCoupons.size());
  }
}
