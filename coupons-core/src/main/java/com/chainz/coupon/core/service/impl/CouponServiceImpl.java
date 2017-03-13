package com.chainz.coupon.core.service.impl;

import com.chainz.coupon.core.config.TimeoutConfig;
import com.chainz.coupon.core.credentials.Operator;
import com.chainz.coupon.core.credentials.OperatorManager;
import com.chainz.coupon.core.exception.CouponInsufficientException;
import com.chainz.coupon.core.exception.CouponNotFoundException;
import com.chainz.coupon.core.exception.CouponStatusConflictException;
import com.chainz.coupon.core.exception.UnAuthorizedOperatorException;
import com.chainz.coupon.core.model.Coupon;
import com.chainz.coupon.core.model.CouponIssuer;
import com.chainz.coupon.core.model.QCoupon;
import com.chainz.coupon.core.redis.CouponGrant;
import com.chainz.coupon.core.repository.CouponRepository;
import com.chainz.coupon.core.service.CouponService;
import com.chainz.coupon.core.utils.Constants;
import com.chainz.coupon.shared.objects.CouponCreateRequest;
import com.chainz.coupon.shared.objects.CouponInfo;
import com.chainz.coupon.shared.objects.CouponIssuerType;
import com.chainz.coupon.shared.objects.CouponStatus;
import com.chainz.coupon.shared.objects.CouponTarget;
import com.chainz.coupon.shared.objects.CouponUpdateRequest;
import com.chainz.coupon.shared.objects.GrantCode;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/** Coupon service implementation. */
@Slf4j
@Service
public class CouponServiceImpl implements CouponService {

  @Autowired private CouponRepository couponRepository;

  @Autowired private MapperFacade mapperFacade;

  @Autowired private RedisTemplate<String, CouponGrant> couponGrantRedisTemplate;

  @Autowired private TimeoutConfig timeoutConfig;

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
    Operator operator = OperatorManager.getOperator();
    if (Constants.SYSTEM.equals(operator.getAccountType())) {
      coupon.setIssuer(new CouponIssuer(CouponIssuerType.SYSTEM));
      coupon.setTarget(CouponTarget.PLATFORM);
    } else {
      coupon.setIssuer(new CouponIssuer(CouponIssuerType.VENDOR, operator.getVendorId()));
      coupon.setTarget(CouponTarget.STORE);
    }
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
    checkPermission(coupon);
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
    checkPermission(coupon);
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
    checkPermission(coupon);
    if (coupon.getStatus() == CouponStatus.INVALID) {
      throw new CouponStatusConflictException(id, coupon.getStatus());
    }
    coupon.setStatus(CouponStatus.INVALID);
    couponRepository.save(coupon);
  }

  @Override
  @Transactional
  @CacheEvict(value = "coupons", key = "#id")
  public void increaseCouponCirculation(Long id, Long increment) {
    Coupon coupon = couponRepository.findOne(id);
    if (coupon == null) {
      throw new CouponNotFoundException(id);
    }
    checkPermission(coupon);
    if (CouponStatus.INVALID == coupon.getStatus()) {
      throw new CouponStatusConflictException(id, coupon.getStatus());
    }
    coupon.setCirculation(coupon.getCirculation() + increment);
    coupon.setSku(coupon.getSku() + increment);
    couponRepository.save(coupon);
  }

  @Override
  @Transactional(readOnly = true)
  public PaginatedApiResult<CouponInfo> listCoupon(
      CouponIssuerType issuerType,
      String issuerId,
      CouponStatus status,
      String q,
      Pageable pageable) {
    QCoupon coupon = QCoupon.coupon;
    BooleanExpression predicate = null;
    if (issuerType != null) {
      if (CouponIssuerType.SYSTEM == issuerType) {
        predicate = coupon.issuer.issuerType.eq(issuerType);
      } else if (issuerId != null) {
        predicate = coupon.issuer.issuerId.eq(issuerId);
      }
    }
    if (status != null) {
      if (predicate == null) {
        predicate = coupon.status.eq(status);
      } else {
        predicate = predicate.and(coupon.status.eq(status));
      }
    }
    if (q != null) {
      if (predicate == null) {
        predicate = coupon.title.containsIgnoreCase(q);
      } else {
        predicate = predicate.and(coupon.title.containsIgnoreCase(q));
      }
    }
    Page<Coupon> coupons = couponRepository.findAll(predicate, pageable);
    return new PaginatedApiResult<>(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        coupons.getNumberOfElements(),
        coupons.getTotalElements(),
        mapperFacade.mapAsList(coupons.getContent(), CouponInfo.class));
  }

  @Override
  @Transactional
  public GrantCode generateCouponGrantCode(Long id, Integer count)
      throws CouponNotFoundException, CouponStatusConflictException, CouponInsufficientException {
    Coupon coupon = couponRepository.findOne(id);
    checkPermission(coupon);
    if (CouponStatus.VERIFIED != coupon.getStatus()) {
      throw new CouponStatusConflictException(id, coupon.getStatus());
    }
    if (coupon.getSku() < count) {
      throw new CouponInsufficientException(id);
    } else {
      CouponGrant couponGrant = new CouponGrant(id, count);
      String key = UUID.randomUUID().toString();
      couponGrantRedisTemplate
          .opsForValue()
          .set(
              Constants.COUPON_GRANT_PREFIX + key,
              couponGrant,
              timeoutConfig.getCouponGrant(),
              TimeUnit.SECONDS);
      return new GrantCode(key);
    }
  }

  /**
   * Check permission.
   *
   * @param coupon coupon instance.
   */
  private void checkPermission(Coupon coupon) {
    Operator operator = OperatorManager.getOperator();
    if (operator.isSystem()) {
      return;
    }
    if (operator.isVendor()
        && coupon.getIssuer().getIssuerType() == CouponIssuerType.VENDOR
        && operator.getVendorId().equals(coupon.getIssuer().getIssuerId())) {
      return;
    }
    throw new UnAuthorizedOperatorException();
  }
}
