package com.chainz.coupon.core.service;

import com.chainz.coupon.core.exception.CouponNotFoundException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.model.QCoupon;
import com.chainz.coupon.core.repository.CouponRepository;
import com.chainz.coupon.shared.objects.CouponCreateRequest;
import com.chainz.coupon.shared.objects.CouponInfo;
import com.chainz.coupon.shared.objects.CouponIssuerType;
import com.chainz.coupon.shared.objects.CouponStatus;
import com.chainz.coupon.shared.objects.CouponUpdateRequest;
import com.chainz.coupon.shared.objects.common.PaginatedApiResult;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Coupon service implementation. */
@Slf4j
@Service
public class CouponServiceImpl implements CouponService {

  @Autowired private CouponRepository couponRepository;

  @Autowired private MapperFacade mapperFacade;

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "coupons", key = "#id")
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
  @CachePut(value = "coupons", key = "#id")
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
  @CacheEvict(value = "coupons", key = "#id")
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
  @CacheEvict(value = "coupons", key = "#id")
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

  @Override
  @Transactional
  @CacheEvict(value = "coupons", key = "#id")
  public void increaseCirculation(Long id, Long increment) {
    Coupon coupon = couponRepository.findOne(id);
    if (coupon == null) {
      throw new CouponNotFoundException(id);
    }
    if (CouponStatus.INVALID == coupon.getStatus()) {
      throw new CouponStatusConflictException(id, coupon.getStatus());
    }
    coupon.setCirculation(coupon.getCirculation() + increment);
    couponRepository.save(coupon);
  }

  @Override
  @Transactional(readOnly = true)
  public PaginatedApiResult<CouponInfo> list(
      CouponIssuerType issuerType, String issuerId, CouponStatus status, Pageable pageable) {
    QCoupon coupon = QCoupon.coupon;
    BooleanExpression predicate = null;
    if (issuerType != null) {
      predicate = coupon.issuer.issuerType.eq(issuerType);
      if (CouponIssuerType.PLATFORM != issuerType && issuerId != null) {
        predicate = predicate.and(coupon.issuer.issuerId.eq(issuerId));
      }
    }
    if (status != null) {
      if (predicate == null) {
        predicate = coupon.status.eq(status);
      } else {
        predicate = predicate.and(coupon.status.eq(status));
      }
    }
    Page<Coupon> coupons = couponRepository.findAll(predicate, pageable);
    return new PaginatedApiResult<>(
        pageable.getPageNumber(),
        coupons.getNumberOfElements(),
        coupons.getTotalElements(),
        mapperFacade.mapAsList(coupons.getContent(), CouponInfo.class));
  }
}
