package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.credentials.ClientPermission;
import com.chainz.coupon.core.exception.SellCouponGrantNotFoundException;
import com.chainz.coupon.core.model.SellCouponGrant;
import com.chainz.coupon.core.repository.SellCouponGrantRepository;
import com.chainz.coupon.core.service.SellCouponGrantService;
import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.shared.objects.SellCouponGrantInfo;
import com.chainz.coupon.shared.objects.SellCouponGrantStatus;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Sell coupon grant service implementation. */
@Service
public class SellCouponGrantServiceImpl implements SellCouponGrantService {

  @Autowired private SellCouponGrantRepository sellCouponGrantRepository;
  @Autowired private MapperFacade mapperFacade;
  @Autowired private StringRedisTemplate stringRedisTemplate;

  @Override
  @ClientPermission
  @Transactional(readOnly = true)
  public SellCouponGrantInfo getSellCouponGrant(String grantCode)
      throws SellCouponGrantNotFoundException {
    SellCouponGrant sellCouponGrant = sellCouponGrantRepository.findOne(grantCode);
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
}
