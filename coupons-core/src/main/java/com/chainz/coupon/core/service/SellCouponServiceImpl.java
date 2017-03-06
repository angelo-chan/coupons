package com.chainz.coupon.core.service;

import com.chainz.coupon.core.credentials.Operator;
import com.chainz.coupon.core.credentials.OperatorManager;
import com.chainz.coupon.core.exception.CouponInsufficientException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.UnAuthorizedOperatorException;
import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.model.SellCoupon;
import com.chainz.coupon.core.redis.CouponGrant;
import com.chainz.coupon.core.repository.CouponRepository;
import com.chainz.coupon.core.repository.SellCouponRepository;
import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.shared.objects.CouponStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/** Sell coupon service implementation. */
@Service
public class SellCouponServiceImpl implements SellCouponService {

  @Autowired private CouponRepository couponRepository;
  @Autowired private SellCouponRepository sellCouponRepository;
  @Autowired private RedisTemplate<String, CouponGrant> couponGrantRedisTemplate;

  @Override
  @Transactional
  public void grant(String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException, CouponInsufficientException {
    checkAccountType();
    String key = Constants.COUPON_GRANT_PREFIX + grantCode;
    CouponGrant couponGrant = couponGrantRedisTemplate.opsForValue().getAndSet(key, null);
    if (couponGrant == null) {
      throw new InvalidGrantCodeException();
    }
    Coupon coupon = couponRepository.findOne(couponGrant.getCouponId());
    if (coupon.getStatus() != CouponStatus.VERIFIED) {
      throw new CouponStatusConflictException(coupon.getId(), coupon.getStatus());
    }
    if (coupon.getSku() < couponGrant.getCount()) {
      throw new CouponInsufficientException(coupon.getId());
    }
    Operator operator = OperatorManager.getOperator();
    String openId = operator.getOpenId();
    Integer count = couponGrant.getCount();
    SellCoupon sellCoupon = sellCouponRepository.findOneByOpenIdAndCoupon(openId, coupon);
    if (sellCoupon == null) {
      sellCoupon = new SellCoupon();
      sellCoupon.setCoupon(coupon);
      sellCoupon.setOpenId(openId);
      sellCoupon.setSku(count);
    } else {
      sellCoupon.setSku(sellCoupon.getSku() + count);
    }
    coupon.setSku(coupon.getSku() - count);
    couponRepository.save(coupon);
    sellCouponRepository.save(sellCoupon);
  }

  /** check account type. */
  private void checkAccountType() {
    Operator operator = OperatorManager.getOperator();
    if (!Constants.CLIENT.equals(operator.getAccountType())) {
      throw new UnAuthorizedOperatorException();
    }
    return;
  }
}
