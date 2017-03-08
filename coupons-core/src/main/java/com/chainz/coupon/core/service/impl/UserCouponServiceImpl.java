package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.credentials.ClientPermission;
import com.chainz.coupon.core.credentials.Operator;
import com.chainz.coupon.core.credentials.OperatorManager;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.InvalidGrantCodeException;
import com.chainz.coupon.core.exception.SellCouponGrantInsufficientException;
import com.chainz.coupon.core.exception.SellCouponGrantStatusConflictException;
import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.model.SellCouponGrant;
import com.chainz.coupon.core.model.UserCoupon;
import com.chainz.coupon.core.repository.SellCouponGrantRepository;
import com.chainz.coupon.core.repository.UserCouponRepository;
import com.chainz.coupon.core.service.UserCouponService;
import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.core.utils.CouponCodes;
import com.chainz.coupon.shared.objects.CouponStatus;
import com.chainz.coupon.shared.objects.OutId;
import com.chainz.coupon.shared.objects.SellCouponGrantStatus;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** User coupon service implementation. */
@Service
public class UserCouponServiceImpl implements UserCouponService {

  @Autowired private StringRedisTemplate stringRedisTemplate;
  @Autowired private MapperFacade mapperFacade;
  @Autowired private SellCouponGrantRepository sellCouponGrantRepository;
  @Autowired private UserCouponRepository userCouponRepository;

  @Override
  @ClientPermission
  @Transactional
  public void granted(String grantCode)
      throws InvalidGrantCodeException, CouponStatusConflictException,
          SellCouponGrantInsufficientException, SellCouponGrantStatusConflictException {
    String key = Constants.SELL_COUPON_GRANT_PREFIX + grantCode;
    String value = stringRedisTemplate.opsForValue().get(key);
    if (value == null) {
      throw new InvalidGrantCodeException();
    }
    Integer remain = Integer.valueOf(value);
    // in order to exclude most scene that already no coupon there
    if (remain < 1) {
      throw new SellCouponGrantInsufficientException(grantCode);
    }
    SellCouponGrant sellCouponGrant = sellCouponGrantRepository.findOne(grantCode);
    if (sellCouponGrant.getStatus() != SellCouponGrantStatus.INPROGRESS) {
      throw new SellCouponGrantStatusConflictException(
          sellCouponGrant.getId(), sellCouponGrant.getStatus());
    }
    Coupon coupon = sellCouponGrant.getSellCoupon().getCoupon();
    if (coupon.getStatus() == CouponStatus.INVALID) {
      throw new CouponStatusConflictException(coupon.getId(), coupon.getStatus());
    }
    if (sellCouponGrant.getRemain() < 1) {
      throw new SellCouponGrantInsufficientException(sellCouponGrant.getId());
    }
    Operator operator = OperatorManager.getOperator();
    sellCouponGrant.decreaseRemain();
    UserCoupon userCoupon = UserCoupon.newFromCoupon(coupon);
    userCoupon.setOpenId(operator.getOpenId());
    userCoupon.setOriginalOpenId(sellCouponGrant.getOpenId());
    userCoupon.setOutId(OutId.SELL);
    String couponCode;
    do {
      couponCode = CouponCodes.generateDefault();
    } while (userCouponRepository.existsByCouponCode(couponCode));
    userCoupon.setCouponCode(couponCode);
    sellCouponGrantRepository.save(sellCouponGrant);
    userCouponRepository.save(userCoupon);
    stringRedisTemplate.opsForValue().increment(key, -1);
  }
}
