package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponNotFoundException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.repository.CouponRepository;
import com.chainz.coupon.shared.objects.CouponCreateRequest;
import com.chainz.coupon.shared.objects.CouponInfo;
import com.chainz.coupon.shared.objects.CouponStatus;
import com.chainz.coupon.shared.objects.CouponUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Coupon service implementation.
 */
@Slf4j
@Service
public class CouponServiceImpl implements CouponService {

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private MapperFacade mapperFacade;

  @Override
  @Transactional(readOnly = true)
  public CouponInfo getCoupon(Long id) {
    Coupon coupon = couponRepository.findOne(id);
    if (coupon == null) {
      throw new CouponNotFoundException(id);
    }
    return mapperFacade.map(coupon, CouponInfo.class);
  }

  @Override
  @Transactional
  public CouponInfo createCoupon(CouponCreateRequest couponCreateRequest) {
    Coupon coupon = mapperFacade.map(couponCreateRequest, Coupon.class);
    coupon.setSku(couponCreateRequest.getCirculation());
    return mapperFacade.map(couponRepository.saveAndFlush(coupon), CouponInfo.class);
  }

  @Override
  @Transactional
  public CouponInfo updateCoupon(Long id, CouponUpdateRequest couponUpdateRequest) {
    Coupon coupon = couponRepository.findOne(id);
    if (coupon == null) {
      throw new CouponNotFoundException(id);
    }
    if (CouponStatus.UNVERIFIED != coupon.getStatus()) {
      throw new CouponStatusConflictException(id, coupon.getStatus());
    }
    mapperFacade.map(couponUpdateRequest, coupon);
    return mapperFacade.map(couponRepository.saveAndFlush(coupon), CouponInfo.class);
  }

  @Override
  @Transactional
  public void verifyCoupon(Long id) {
    Coupon coupon = couponRepository.findOne(id);
    if (coupon == null) {
      throw new CouponNotFoundException(id);
    }
    if (CouponStatus.UNVERIFIED != coupon.getStatus()) {
      throw new CouponStatusConflictException(id, coupon.getStatus());
    }
    coupon.setStatus(CouponStatus.VERIFIED);
    couponRepository.save(coupon);
  }

  @Override
  @Transactional
  public void invalidCoupon(Long id) {
    Coupon coupon = couponRepository.findOne(id);
    if (coupon == null) {
      throw new CouponNotFoundException(id);
    }
    if (coupon.getStatus() == CouponStatus.INVALID) {
      throw new CouponStatusConflictException(id, coupon.getStatus());
    }
    coupon.setStatus(CouponStatus.INVALID);
    couponRepository.save(coupon);
  }

}
