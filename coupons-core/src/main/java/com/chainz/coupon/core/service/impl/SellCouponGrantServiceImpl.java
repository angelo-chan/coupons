package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.credentials.ClientPermission;
import com.chainz.coupon.core.exception.SellCouponGrantNotFoundException;
import com.chainz.coupon.core.model.SellCouponGrant;
import com.chainz.coupon.core.repository.SellCouponGrantRepository;
import com.chainz.coupon.core.service.SellCouponGrantService;
import com.chainz.coupon.shared.objects.SellCouponGrantInfo;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Sell coupon grant service implementation. */
@Service
public class SellCouponGrantServiceImpl implements SellCouponGrantService {

  @Autowired private SellCouponGrantRepository sellCouponGrantRepository;
  @Autowired private MapperFacade mapperFacade;

  @Override
  @ClientPermission
  @Transactional(readOnly = true)
  public SellCouponGrantInfo getSellCouponGrant(String grantCode)
      throws SellCouponGrantNotFoundException {
    SellCouponGrant sellCouponGrant = sellCouponGrantRepository.findOne(grantCode);
    if (sellCouponGrant == null) {
      throw new SellCouponGrantNotFoundException(grantCode);
    }
    return mapperFacade.map(sellCouponGrant, SellCouponGrantInfo.class);
  }
}
