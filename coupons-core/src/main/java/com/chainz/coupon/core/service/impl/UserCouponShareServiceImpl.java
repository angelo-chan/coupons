package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.credentials.ClientPermission;
import com.chainz.coupon.core.exception.UserCouponShareNotFoundException;
import com.chainz.coupon.core.model.QUserCouponShare;
import com.chainz.coupon.core.model.UserCouponShare;
import com.chainz.coupon.core.repository.UserCouponShareRepository;
import com.chainz.coupon.core.repository.common.JoinDescriptor;
import com.chainz.coupon.core.service.UserCouponShareService;
import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.shared.objects.UserCouponShareInfo;
import com.chainz.coupon.shared.objects.UserCouponShareStatus;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** User coupon share service implementation. */
@Service
public class UserCouponShareServiceImpl implements UserCouponShareService {

  @Autowired private UserCouponShareRepository userCouponShareRepository;
  @Autowired private MapperFacade mapperFacade;
  @Autowired private StringRedisTemplate stringRedisTemplate;

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
}
