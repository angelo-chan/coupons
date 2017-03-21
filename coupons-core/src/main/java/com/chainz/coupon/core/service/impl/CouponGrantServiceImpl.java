package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.exception.CouponGrantNotFoundException;
import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.redis.CouponGrant;
import com.chainz.coupon.core.repository.CouponRepository;
import com.chainz.coupon.core.service.CouponGrantService;
import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.shared.objects.BulkCouponInfo;
import com.chainz.coupon.shared.objects.CouponGrantInfo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Coupon grant service implementation. */
@Slf4j
@Service
public class CouponGrantServiceImpl implements CouponGrantService {
  @Autowired private CouponRepository couponRepository;
  @Autowired private RedisTemplate<String, CouponGrant> couponGrantRedisTemplate;
  @Autowired private MapperFacade mapperFacade;

  @Override
  @Transactional(readOnly = true)
  public CouponGrantInfo getCouponGrant(String grantCode) throws CouponGrantNotFoundException {
    String key = Constants.COUPON_GRANT_PREFIX + grantCode;
    CouponGrant couponGrant = couponGrantRedisTemplate.opsForValue().get(key);
    if (couponGrant == null) {
      throw new CouponGrantNotFoundException(grantCode);
    }
    Coupon coupon = couponRepository.findOne(couponGrant.getCouponId());
    CouponGrantInfo couponGrantInfo = new CouponGrantInfo();
    BulkCouponInfo bulkCouponInfo = mapperFacade.map(coupon, BulkCouponInfo.class);
    couponGrantInfo.setCoupon(bulkCouponInfo);
    couponGrantInfo.setGrantCode(grantCode);
    couponGrantInfo.setSku(couponGrant.getCount());
    return couponGrantInfo;
  }
}
