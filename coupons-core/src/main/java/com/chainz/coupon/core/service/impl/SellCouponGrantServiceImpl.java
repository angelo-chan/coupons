package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.config.SchedulerConfig;
import com.chainz.coupon.core.config.TimeoutConfig;
import com.chainz.coupon.core.credentials.ClientPermission;
import com.chainz.coupon.core.credentials.Operator;
import com.chainz.coupon.core.credentials.OperatorManager;
import com.chainz.coupon.core.exception.SellCouponGrantNotFoundException;
import com.chainz.coupon.core.exception.SellCouponGrantStatusConflictException;
import com.chainz.coupon.core.model.QSellCouponGrant;
import com.chainz.coupon.core.model.QSellCouponGrantEntry;
import com.chainz.coupon.core.model.SellCoupon;
import com.chainz.coupon.core.model.SellCouponGrant;
import com.chainz.coupon.core.repository.SellCouponGrantEntryRepository;
import com.chainz.coupon.core.repository.SellCouponGrantRepository;
import com.chainz.coupon.core.repository.SellCouponRepository;
import com.chainz.coupon.core.repository.common.JoinDescriptor;
import com.chainz.coupon.core.service.SellCouponGrantService;
import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.shared.objects.SellCouponGrantInfo;
import com.chainz.coupon.shared.objects.SellCouponGrantStatus;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
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

/** Sell coupon grant service implementation. */
@Slf4j
@Service
public class SellCouponGrantServiceImpl implements SellCouponGrantService {

  @Autowired private SellCouponRepository sellCouponRepository;
  @Autowired private SellCouponGrantRepository sellCouponGrantRepository;
  @Autowired private SellCouponGrantEntryRepository sellCouponGrantEntryRepository;
  @Autowired private MapperFacade mapperFacade;
  @Autowired private StringRedisTemplate stringRedisTemplate;
  @Autowired private TimeoutConfig timeoutConfig;
  @Autowired private SchedulerConfig schedulerConfig;
  @PersistenceContext private EntityManager entityManager;

  @Override
  @ClientPermission
  @Transactional(readOnly = true)
  public SellCouponGrantInfo getSellCouponGrant(String grantCode)
      throws SellCouponGrantNotFoundException {
    // use predicate to query since we want to do join avoid lazy load
    QSellCouponGrant qSellCouponGrant = QSellCouponGrant.sellCouponGrant;
    SellCouponGrant sellCouponGrant =
        sellCouponGrantRepository.findOne(
            qSellCouponGrant.id.eq(grantCode), JoinDescriptor.join(qSellCouponGrant.sellCoupon));
    if (sellCouponGrant == null) {
      throw new SellCouponGrantNotFoundException(grantCode);
    }
    SellCouponGrantInfo sellCouponGrantInfo =
        mapperFacade.map(sellCouponGrant, SellCouponGrantInfo.class);
    if (sellCouponGrantInfo.getStatus() == SellCouponGrantStatus.INPROGRESS) {
      String key = Constants.SELL_COUPON_GRANT_PREFIX + grantCode;
      String remain = stringRedisTemplate.opsForValue().get(key);
      if (remain != null) {
        Integer remainInt = Integer.valueOf(remain);
        sellCouponGrantInfo.setRemain(Integer.max(remainInt, 0));
      }
    }
    return sellCouponGrantInfo;
  }

  @Override
  @ClientPermission
  @Transactional
  public void abortSellCouponGrant(String grantCode)
      throws SellCouponGrantNotFoundException, SellCouponGrantStatusConflictException {
    Operator operator = OperatorManager.getOperator();
    String openId = operator.getOpenId();
    QSellCouponGrant qSellCouponGrant = QSellCouponGrant.sellCouponGrant;
    Predicate predicate = qSellCouponGrant.id.eq(grantCode).and(qSellCouponGrant.openId.eq(openId));
    SellCouponGrant sellCouponGrant =
        sellCouponGrantRepository.findOne(
            predicate, JoinDescriptor.join(qSellCouponGrant.sellCoupon));
    if (sellCouponGrant == null) {
      throw new SellCouponGrantNotFoundException(grantCode);
    }
    if (sellCouponGrant.getStatus() != SellCouponGrantStatus.INPROGRESS) {
      throw new SellCouponGrantStatusConflictException(
          sellCouponGrant.getId(), sellCouponGrant.getStatus());
    }
    cancelSellCouponGrant(sellCouponGrant, SellCouponGrantStatus.ABORTED);
    String key = Constants.SELL_COUPON_GRANT_PREFIX + grantCode;
    stringRedisTemplate.delete(key);
  }

  @Override
  @Transactional
  public void checkSellCouponGrants() {
    QSellCouponGrant qSellCouponGrant = QSellCouponGrant.sellCouponGrant;
    ZonedDateTime now = ZonedDateTime.now();
    int sellCouponGrantTimeout = timeoutConfig.getSellCouponGrant();
    long threshold = schedulerConfig.getThreshold().getSellCouponGrant();
    ZonedDateTime end = now.minus(sellCouponGrantTimeout, ChronoUnit.SECONDS);
    ZonedDateTime start = end.minus(threshold, ChronoUnit.SECONDS);

    Predicate predicate =
        qSellCouponGrant
            .createdAt
            .after(start)
            .and(qSellCouponGrant.createdAt.before(end))
            .and(qSellCouponGrant.status.eq(SellCouponGrantStatus.INPROGRESS));
    JPAQuery<Void> query = new JPAQuery<>(entityManager);
    List<String> ids =
        query.from(qSellCouponGrant).select(qSellCouponGrant.id).where(predicate).fetch();
    log.info("found [{}] expired sell coupon grants.", ids.size());
    for (String id : ids) {
      try {
        expireSellCouponGrant(id);
      } catch (RuntimeException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  /**
   * Expire sell coupon grant.
   *
   * @param id sell coupon id.
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void expireSellCouponGrant(String id) {
    log.info("begin to expire sell coupon grant [{}]", id);
    QSellCouponGrant qSellCouponGrant = QSellCouponGrant.sellCouponGrant;
    Predicate predicate =
        qSellCouponGrant
            .id
            .eq(id)
            .and(qSellCouponGrant.status.eq(SellCouponGrantStatus.INPROGRESS));
    SellCouponGrant sellCouponGrant =
        sellCouponGrantRepository.findOne(
            predicate, JoinDescriptor.join(qSellCouponGrant.sellCoupon));
    if (sellCouponGrant == null) {
      return;
    }
    cancelSellCouponGrant(sellCouponGrant, SellCouponGrantStatus.EXPIRED);
    log.info("success to expire sell coupon grant [{}]", id);
  }

  /**
   * Cancel sell coupon grant.
   *
   * @param sellCouponGrant sell coupon grant.
   * @param destStatus destination status.
   */
  private void cancelSellCouponGrant(
      SellCouponGrant sellCouponGrant, SellCouponGrantStatus destStatus) {
    log.debug("begin to reset sell coupon grant: {}", sellCouponGrant.getId());
    QSellCouponGrantEntry qSellCouponGrantEntry = QSellCouponGrantEntry.sellCouponGrantEntry;
    Long grantedCount =
        sellCouponGrantEntryRepository.count(
            qSellCouponGrantEntry.sellCouponGrant.eq(sellCouponGrant));
    log.debug(
        "found granted count: {} of {} for sell coupon grant: {}",
        grantedCount,
        sellCouponGrant.getCount(),
        sellCouponGrant.getId());
    sellCouponGrant.setStatus(destStatus);
    Integer returnCount = sellCouponGrant.getCount() - grantedCount.intValue();
    SellCoupon sellCoupon = sellCouponGrant.getSellCoupon();
    sellCoupon.setSku(sellCoupon.getSku() + returnCount);
    sellCouponGrantRepository.save(sellCouponGrant);
    sellCouponRepository.save(sellCoupon);
    log.debug(
        "success to reset sell coupon grant: {}, and return {} sell coupon",
        sellCouponGrant.getId(),
        returnCount);
  }
}
