package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.config.TimeoutConfig;
import com.chainz.coupon.core.credentials.ClientPermission;
import com.chainz.coupon.core.credentials.Operator;
import com.chainz.coupon.core.credentials.OperatorManager;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.InvalidShareCodeException;
import com.chainz.coupon.core.exception.UserCouponNotFoundException;
import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.model.QSellCouponGrant;
import com.chainz.coupon.core.model.QUserCoupon;
import com.chainz.coupon.core.model.SellCouponGrant;
import com.chainz.coupon.core.model.SellCouponGrantEntry;
import com.chainz.coupon.core.model.UserCoupon;
import com.chainz.coupon.core.model.UserCouponShare;
import com.chainz.coupon.core.model.UserCouponShareEntry;
import com.chainz.coupon.core.repository.CouponRepository;
import com.chainz.coupon.core.repository.SellCouponGrantEntryRepository;
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
import com.chainz.coupon.shared.objects.UserCouponReturnRequest;
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
import java.time.Duration;
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
  @Autowired private SellCouponGrantEntryRepository sellCouponGrantEntryRepository;
  @Autowired private UserCouponRepository userCouponRepository;
  @Autowired private CouponRepository couponRepository;
  @Autowired private EntityManager entityManager;
  @Autowired private UserCouponShareRepository userCouponShareRepository;
  @Autowired private TimeoutConfig timeoutConfig;

  @Override
  @ClientPermission
  @Transactional
  public void granted(String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException {
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
      // use predicate to query since we want to do join avoid lazy load
      QSellCouponGrant qSellCouponGrant = QSellCouponGrant.sellCouponGrant;
      SellCouponGrant sellCouponGrant =
          sellCouponGrantRepository.findOne(
              qSellCouponGrant.id.eq(grantCode), JoinDescriptor.join(qSellCouponGrant.sellCoupon));
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
          new SellCouponGrantEntry(
              sellCouponGrant, sellCouponGrant.getOpenId(), operator.getOpenId(), couponCode);
      if (value == 0) {
        sellCouponGrant.setStatus(SellCouponGrantStatus.COMPLETED);
        sellCouponGrantRepository.save(sellCouponGrant);
      }
      sellCouponGrantEntryRepository.save(sellCouponGrantEntry);
      userCouponRepository.save(userCoupon);
    } catch (RuntimeException e) {
      stringRedisTemplate.opsForValue().increment(key, 1);
      throw e;
    }
  }

  @Override
  @ClientPermission
  @Transactional
  public void shared(String shareCode) throws InvalidShareCodeException {
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
    ZonedDateTime now = ZonedDateTime.now();
    UserCouponShare userCouponShare = null;

    try {
      userCouponShare = userCouponShareRepository.findOne(shareCode);

      Operator operator = OperatorManager.getOperator();
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
      if (count == 1 && userCouponShare != null) {
        // the list has been deleted after the pop, so will reset the expire for the new push
        stringRedisTemplate.expire(
            key,
            Duration.between(now, userCouponShare.getExpiredAt()).getSeconds(),
            TimeUnit.SECONDS);
      }
      throw e;
    }
  }

  @Override
  @ClientPermission
  @Transactional(readOnly = true)
  public UserCouponInfo getUserCoupon(Long userCouponId) throws UserCouponNotFoundException {
    // use predicate and join to avoid lazy load coupon
    QUserCoupon qUserCoupon = QUserCoupon.userCoupon;
    UserCoupon userCoupon =
        userCouponRepository.findOne(
            qUserCoupon.id.eq(userCouponId), JoinDescriptor.join(qUserCoupon.coupon));
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
            .and(userCoupon.endDate.goe(LocalDate.now()))
            .and(userCoupon.status.eq(UserCouponStatus.UNUSED));
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
            .and(userCoupon.endDate.lt(LocalDate.now()))
            .and(userCoupon.status.eq(UserCouponStatus.UNUSED));
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
            .id
            .in(userCouponIdList)
            .and(userCoupon.openId.eq(openId))
          .and(userCoupon.storeId.eq(storeId))
          .and(userCoupon.beginDate.loe(now))
            .and(userCoupon.endDate.goe(now))
            .and(userCoupon.status.eq(UserCouponStatus.UNUSED));
    JPAQuery<Void> query = new JPAQuery<>(entityManager);
    long count = query.from(userCoupon).where(predicate).fetchCount();
    if (count != userCouponIdList.size()) {
      throw new UserCouponNotFoundException(userCouponIdList);
    }
    predicate = userCoupon.id.in(userCouponIdList);
    new JPAUpdateClause(entityManager, userCoupon)
        .where(predicate)
        .set(userCoupon.status, UserCouponStatus.USED)
        .set(userCoupon.consumedAt, ZonedDateTime.now())
        .execute();
  }

  @Override
  @Transactional
  public void returnUserCoupon(UserCouponReturnRequest userCouponReturnRequest)
      throws UserCouponNotFoundException {
    List<Long> userCouponIdList = userCouponReturnRequest.getIds();
    Operator operator = OperatorManager.getOperator();
    if (!(operator.isClient() || operator.isStore())) {
      throw new UserCouponNotFoundException(userCouponIdList);
    }
    JPAQuery<Void> query = new JPAQuery<>(entityManager);
    QUserCoupon userCoupon = QUserCoupon.userCoupon;
    BooleanExpression predicate =
        userCoupon.id.in(userCouponIdList).and(userCoupon.status.eq(UserCouponStatus.USED));
    if (operator.isClient()) {
      String openId = operator.getOpenId();
      predicate = predicate.and(userCoupon.openId.eq(openId));
    } else {
      // should be store
      String storeId = operator.getStoreId();
      predicate = predicate.and(userCoupon.storeId.eq(storeId));
    }
    long count = query.from(userCoupon).where(predicate).fetchCount();
    if (count != userCouponIdList.size()) {
      throw new UserCouponNotFoundException(userCouponIdList);
    }
    predicate = userCoupon.id.in(userCouponIdList);
    new JPAUpdateClause(entityManager, userCoupon)
        .where(predicate)
        .set(userCoupon.status, UserCouponStatus.UNUSED)
        .set(userCoupon.storeId, (String) null)
        .set(userCoupon.consumedAt, (ZonedDateTime) null)
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
            .id
            .in(userCouponIdList)
            .and(userCoupon.openId.eq(openId))
            .and(userCoupon.coupon.id.eq(couponId))
            .and(userCoupon.endDate.goe(now))
            .and(userCoupon.status.eq(UserCouponStatus.UNUSED));
    JPAQuery<Void> query = new JPAQuery<>(entityManager);
    long count = query.from(userCoupon).where(predicate).fetchCount();
    if (count != userCouponIdList.size()) {
      throw new UserCouponNotFoundException(userCouponIdList);
    }
    Coupon coupon = couponRepository.findOne(couponId);
    UserCouponShare userCouponShare =
        UserCouponShare.newInstance(coupon, userCouponIdList, timeoutConfig.getUserCouponShare());
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
    stringRedisTemplate.expire(key, timeoutConfig.getUserCouponShare(), TimeUnit.SECONDS);
    return new ShareCode(userCouponShare.getId());
  }
}
