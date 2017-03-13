package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.credentials.ClientPermission;
import com.chainz.coupon.core.credentials.Operator;
import com.chainz.coupon.core.credentials.OperatorManager;
import com.chainz.coupon.core.exception.SellCouponGrantNotFoundException;
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
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Sell coupon grant service implementation. */
@Slf4j
@Service
public class SellCouponGrantServiceImpl implements SellCouponGrantService {

  @Autowired private SellCouponRepository sellCouponRepository;
  @Autowired private SellCouponGrantRepository sellCouponGrantRepository;
  @Autowired private SellCouponGrantEntryRepository sellCouponGrantEntryRepository;
  @Autowired private MapperFacade mapperFacade;
  @Autowired private StringRedisTemplate stringRedisTemplate;

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
  public void abortSellCouponGrant(String grantCode) throws SellCouponGrantNotFoundException {
    Operator operator = OperatorManager.getOperator();
    String openId = operator.getOpenId();
    QSellCouponGrant qSellCouponGrant = QSellCouponGrant.sellCouponGrant;
    Predicate predicate =
        qSellCouponGrant
            .id
            .eq(grantCode)
            .and(qSellCouponGrant.openId.eq(openId))
            .and(qSellCouponGrant.status.eq(SellCouponGrantStatus.INPROGRESS));
    SellCouponGrant sellCouponGrant =
        sellCouponGrantRepository.findOne(
            predicate, JoinDescriptor.join(qSellCouponGrant.sellCoupon));
    if (sellCouponGrant == null) {
      throw new SellCouponGrantNotFoundException(grantCode);
    }
    log.debug("begin to abort sell coupon grant: {}", grantCode);
    sellCouponGrant.setStatus(SellCouponGrantStatus.ABORTED);
    QSellCouponGrantEntry qSellCouponGrantEntry = QSellCouponGrantEntry.sellCouponGrantEntry;
    Long grantedCount =
        sellCouponGrantEntryRepository.count(
            qSellCouponGrantEntry.sellCouponGrant.eq(sellCouponGrant));

    log.debug("found granted count: {} for sell coupon grant: {}", grantedCount, grantCode);
    Integer returnCount = sellCouponGrant.getCount() - grantedCount.intValue();
    SellCoupon sellCoupon = sellCouponGrant.getSellCoupon();
    sellCoupon.setSku(sellCoupon.getSku() + returnCount);
    sellCouponGrantRepository.save(sellCouponGrant);
    sellCouponRepository.save(sellCoupon);
    log.debug(
        "success to abort sell coupon grant: {}, and return {} sell coupon",
        grantCode,
        returnCount);
  }
}
