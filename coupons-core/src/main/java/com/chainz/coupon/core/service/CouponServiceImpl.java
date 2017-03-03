package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponNotFoundException;
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

import javax.transaction.Transactional;

/** Coupon service implementation. */
@Slf4j
@Service
public class CouponServiceImpl implements CouponService {

  @Autowired private CouponRepository couponRepository;

  @Autowired private MapperFacade mapperFacade;

  @Transactional
  @Override
  public CouponInfo createCoupon(CouponCreateRequest couponCreateRequest) {
    Coupon coupon = mapperFacade.map(couponCreateRequest, Coupon.class);
    coupon.setSku(couponCreateRequest.getCirculation());
    return mapperFacade.map(couponRepository.saveAndFlush(coupon), CouponInfo.class);
  }

  @Transactional
  @Override
  public CouponInfo updateCoupon(String id, CouponUpdateRequest couponUpdateRequest) {
    Coupon coupon = couponRepository.findOne(id);
    if (coupon == null) {
      throw new CouponNotFoundException(id);
    }
    mapperFacade.map(couponUpdateRequest, coupon);
    return mapperFacade.map(couponRepository.saveAndFlush(coupon), CouponInfo.class);
  }

  @Transactional
  @Override
  public void verifyCoupon(String id) {
    Coupon coupon = couponRepository.findOne(id);
    if (coupon == null) {
      throw new CouponNotFoundException(id);
    }
    if (coupon.getStatus() == CouponStatus.VERIFIED) {
      return;
    }
    coupon.setStatus(CouponStatus.VERIFIED);
    couponRepository.saveAndFlush(coupon);
  }

  @Transactional
  @Override
  public void invalidCoupon(String id) {
    Coupon coupon = couponRepository.findOne(id);
    if (coupon == null) {
      throw new CouponNotFoundException(id);
    }
    if (coupon.getStatus() == CouponStatus.INVALID) {
      return;
    }
    coupon.setStatus(CouponStatus.INVALID);
    couponRepository.saveAndFlush(coupon);
  }

}
