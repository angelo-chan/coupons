package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.credentials.ClientPermission;
import com.chainz.coupon.core.credentials.Operator;
import com.chainz.coupon.core.credentials.OperatorManager;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.InvalidShareCodeException;
import com.chainz.coupon.core.exception.SellCouponGrantStatusConflictException;
import com.chainz.coupon.core.exception.UserCouponNotFoundException;
import com.chainz.coupon.core.exception.UserCouponShareStatusConflictException;
import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.model.QUserCoupon;
import com.chainz.coupon.core.model.SellCouponGrant;
import com.chainz.coupon.core.model.SellCouponGrantEntry;
import com.chainz.coupon.core.model.UserCoupon;
import com.chainz.coupon.core.model.UserCouponShare;
import com.chainz.coupon.core.model.UserCouponShareEntry;
import com.chainz.coupon.core.repository.CouponRepository;
import com.chainz.coupon.core.repository.SellCouponGrantRepository;
import com.chainz.coupon.core.repository.UserCouponRepository;
import com.chainz.coupon.core.repository.UserCouponShareRepository;
import com.chainz.coupon.core.repository.common.JoinDescriptor;
import com.chainz.coupon.core.service.UserCouponService;
import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.core.utils.CouponCodes;
import com.chainz.coupon.shared.objects.CouponStatus;
import com.chainz.coupon.shared.objects.OutId;
import com.chainz.coupon.shared.objects.SellCouponGrantStatus;
import com.chainz.coupon.shared.objects.ShareCode;
import com.chainz.coupon.shared.objects.SimpleUserCouponInfo;
import com.chainz.coupon.shared.objects.UserCouponConsumeRequest;
import com.chainz.coupon.shared.objects.UserCouponInfo;
import com.chainz.coupon.shared.objects.UserCouponShareRequest;
import com.chainz.coupon.shared.objects.UserCouponShareStatus;
import com.chainz.coupon.shared.objects.UserCouponStatus;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/** User coupon service implementation. */
@Service
public class UserCouponServiceImpl implements UserCouponService {

  @Autowired private StringRedisTemplate stringRedisTemplate;
  @Autowired private MapperFacade mapperFacade;
  @Autowired private SellCouponGrantRepository sellCouponGrantRepository;
  @Autowired private UserCouponRepository userCouponRepository;
  @Autowired private CouponRepository couponRepository;
  @Autowired private EntityManager entityManager;
  @Autowired private UserCouponShareRepository userCouponShareRepository;

  @Override
  @ClientPermission
  @Transactional
  public void granted(String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException,
          SellCouponGrantStatusConflictException {
    String key = Constants.SELL_COUPON_GRANT_PREFIX + grantCode;
    String valueString = stringRedisTemplate.opsForValue().get(key);
    if (valueString == null) {
      throw new InvalidGrantCodeException(grantCode);
    }
    Long value = stringRedisTemplate.opsForValue().increment(key, -1);
    if (value < 0) {
      stringRedisTemplate.delete(key);
      throw new InvalidGrantCodeException(grantCode);
    }

    try {
      SellCouponGrant sellCouponGrant = sellCouponGrantRepository.findOne(grantCode);
      if (sellCouponGrant.getStatus() != SellCouponGrantStatus.INPROGRESS) {
        throw new SellCouponGrantStatusConflictException(
            sellCouponGrant.getId(), sellCouponGrant.getStatus());
      }
      Coupon coupon = sellCouponGrant.getSellCoupon().getCoupon();
      if (coupon.getStatus() == CouponStatus.INVALID) {
        throw new CouponStatusConflictException(coupon.getId(), coupon.getStatus());
      }

      // new user coupon
      Operator operator = OperatorManager.getOperator();
      UserCoupon userCoupon = UserCoupon.newFromCoupon(coupon);
      userCoupon.setOpenId(operator.getOpenId());
      userCoupon.setOutId(OutId.SELL);
      String couponCode;
      do {
        couponCode = CouponCodes.generateDefault();
      } while (userCouponRepository.existsByCouponCode(couponCode));
      userCoupon.setCouponCode(couponCode);

      // new sell coupon grant entry
      SellCouponGrantEntry sellCouponGrantEntry =
          new SellCouponGrantEntry(sellCouponGrant.getOpenId(), operator.getOpenId(), couponCode);
      if (value == 0) {
        sellCouponGrant.setStatus(SellCouponGrantStatus.COMPLETED);
      }
      sellCouponGrant.addSellCouponGrantEntry(sellCouponGrantEntry);

      sellCouponGrantRepository.save(sellCouponGrant);
      userCouponRepository.save(userCoupon);
    } catch (RuntimeException e) {
      stringRedisTemplate.opsForValue().increment(key, 1);
      throw e;
    }
  }

  @Override
  @ClientPermission
  @Transactional
  public void shared(String shareCode)
      throws InvalidShareCodeException, UserCouponShareStatusConflictException {
    String key = Constants.USER_COUPON_SHARE_PREFIX + shareCode;
    long count = stringRedisTemplate.opsForList().size(key);
    if (count == 0) {
      throw new InvalidShareCodeException(shareCode);
    }
    String value = stringRedisTemplate.opsForList().leftPop(key);
    if (value == null) {
      throw new InvalidShareCodeException(shareCode);
    }
    Long userCouponId = Long.parseLong(value);

    try {
      UserCouponShare userCouponShare = userCouponShareRepository.findOne(shareCode);
      if (userCouponShare.getStatus() != UserCouponShareStatus.INPROGRESS) {
        throw new UserCouponShareStatusConflictException(
            userCouponShare.getId(), userCouponShare.getStatus());
      }

      Operator operator = OperatorManager.getOperator();
      ZonedDateTime now = ZonedDateTime.now();
      // set user coupon
      UserCoupon userCoupon = userCouponRepository.findOne(userCouponId);
      userCoupon.setFromOpenId(userCoupon.getOpenId());
      userCoupon.setFromUserId(userCoupon.getUserId());
      userCoupon.setOpenId(operator.getOpenId());
      userCoupon.setUserId(null);
      userCoupon.setStatus(UserCouponStatus.UNUSED);
      userCoupon.setGotAt(now);

      // set user coupon share entry
      UserCouponShareEntry userCouponShareEntry =
          userCouponShare.getUserCouponShareEntries().get(userCouponId);
      userCouponShareEntry.setGotAt(now);
      userCouponShareEntry.setGot(true);
      userCouponShareEntry.setOpenId(operator.getOpenId());

      if (count == 1) {
        // should be 0 after this acton
        userCouponShare.setStatus(UserCouponShareStatus.COMPLETED);
      }
      userCouponRepository.save(userCoupon);
      userCouponShareRepository.save(userCouponShare);
    } catch (RuntimeException e) {
      stringRedisTemplate.opsForList().rightPush(key, value);
      throw e;
    }
  }

  @Override
  @ClientPermission
  @Transactional(readOnly = true)
  public UserCouponInfo getUserCoupon(Long userCouponId) throws UserCouponNotFoundException {
    UserCoupon userCoupon = userCouponRepository.findOne(userCouponId);
    Operator operator = OperatorManager.getOperator();
    if (userCoupon == null || !operator.getOpenId().equals(userCoupon.getOpenId())) {
      throw new UserCouponNotFoundException(userCouponId);
    }
    return mapperFacade.map(userCoupon, UserCouponInfo.class);
  }

  @ClientPermission
  @Transactional(readOnly = true)
  @Override
  public PaginatedApiResult<SimpleUserCouponInfo> listActiveUserCoupon(Pageable pageable) {
    Operator operator = OperatorManager.getOperator();
    String openId = operator.getOpenId();
    QUserCoupon userCoupon = QUserCoupon.userCoupon;
    BooleanExpression predicate =
        userCoupon
            .openId
            .eq(openId)
            .and(userCoupon.status.eq(UserCouponStatus.UNUSED))
            .and(userCoupon.endDate.goe(LocalDate.now()));
    Page<UserCoupon> userCoupons =
        userCouponRepository.findAll(predicate, pageable, JoinDescriptor.join(userCoupon.coupon));
    return new PaginatedApiResult<>(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        userCoupons.getNumberOfElements(),
        userCoupons.getTotalElements(),
        mapperFacade.mapAsList(userCoupons.getContent(), SimpleUserCouponInfo.class));
  }

  @ClientPermission
  @Override
  @Transactional(readOnly = true)
  public PaginatedApiResult<SimpleUserCouponInfo> listExpiredUserCoupon(Pageable pageable) {
    Operator operator = OperatorManager.getOperator();
    String openId = operator.getOpenId();
    QUserCoupon userCoupon = QUserCoupon.userCoupon;
    BooleanExpression predicate =
        userCoupon
            .openId
            .eq(openId)
            .and(userCoupon.status.eq(UserCouponStatus.UNUSED))
            .and(userCoupon.endDate.lt(LocalDate.now()));
    Page<UserCoupon> userCoupons =
        userCouponRepository.findAll(predicate, pageable, JoinDescriptor.join(userCoupon.coupon));
    return new PaginatedApiResult<>(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        userCoupons.getNumberOfElements(),
        userCoupons.getTotalElements(),
        mapperFacade.mapAsList(userCoupons.getContent(), SimpleUserCouponInfo.class));
  }

  @ClientPermission
  @Override
  @Transactional
  public void consumeUserCoupon(UserCouponConsumeRequest userCouponConsumeRequest)
      throws UserCouponNotFoundException {
    List<Long> userCouponIdList = userCouponConsumeRequest.getIds();
    String storeId = userCouponConsumeRequest.getStoreId();
    Operator operator = OperatorManager.getOperator();
    String openId = operator.getOpenId();
    QUserCoupon userCoupon = QUserCoupon.userCoupon;
    LocalDate now = LocalDate.now();
    BooleanExpression predicate =
        userCoupon
            .openId
            .eq(openId)
            .and(userCoupon.beginDate.loe(now))
            .and(userCoupon.endDate.goe(now))
            .and(userCoupon.status.eq(UserCouponStatus.UNUSED))
            .and(userCoupon.id.in(userCouponIdList));
    JPAQuery<Void> query = new JPAQuery<>(entityManager);
    long count = query.from(userCoupon).where(predicate).fetchCount();
    if (count != userCouponIdList.size()) {
      throw new UserCouponNotFoundException(userCouponIdList);
    }
    predicate = userCoupon.id.in(userCouponIdList);
    new JPAUpdateClause(entityManager, userCoupon)
        .where(predicate)
        .set(userCoupon.status, UserCouponStatus.USED)
        .set(userCoupon.storeId, storeId)
        .execute();
  }

  @ClientPermission
  @Override
  @Transactional
  public ShareCode shareUserCoupon(UserCouponShareRequest userCouponShareRequest)
      throws UserCouponNotFoundException {
    Long couponId = userCouponShareRequest.getCouponId();
    List<Long> userCouponIdList = userCouponShareRequest.getIds();
    Operator operator = OperatorManager.getOperator();
    String openId = operator.getOpenId();
    QUserCoupon userCoupon = QUserCoupon.userCoupon;
    LocalDate now = LocalDate.now();
    BooleanExpression predicate =
        userCoupon
            .openId
            .eq(openId)
            .and(userCoupon.coupon.id.eq(couponId))
            .and(userCoupon.endDate.goe(now))
            .and(userCoupon.status.eq(UserCouponStatus.UNUSED))
            .and(userCoupon.id.in(userCouponIdList));
    JPAQuery<Void> query = new JPAQuery<>(entityManager);
    long count = query.from(userCoupon).where(predicate).fetchCount();
    if (count != userCouponIdList.size()) {
      throw new UserCouponNotFoundException(userCouponIdList);
    }
    Coupon coupon = couponRepository.findOne(couponId);
    UserCouponShare userCouponShare = UserCouponShare.newInstance(coupon, userCouponIdList);
    userCouponShare.setOpenId(openId);
    userCouponShareRepository.save(userCouponShare);
    predicate = userCoupon.id.in(userCouponIdList);
    new JPAUpdateClause(entityManager, userCoupon)
        .where(predicate)
        .set(userCoupon.status, UserCouponStatus.SHARING)
        .execute();
    String key = Constants.USER_COUPON_SHARE_PREFIX + userCouponShare.getId();
    List<String> userCouponIdStringList =
        userCouponIdList.stream().map(String::valueOf).collect(Collectors.toList());
    stringRedisTemplate.opsForList().leftPushAll(key, userCouponIdStringList);
    stringRedisTemplate.expire(key, Constants.USER_COUPON_SHARE_TIMEOUT, TimeUnit.SECONDS);
    return new ShareCode(userCouponShare.getId());
  }
}
