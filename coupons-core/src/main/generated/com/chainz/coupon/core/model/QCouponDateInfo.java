package com.chainz.coupon.core.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponDateInfo is a Querydsl query type for CouponDateInfo
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QCouponDateInfo extends BeanPath<CouponDateInfo> {

    private static final long serialVersionUID = -222912906L;

    public static final QCouponDateInfo couponDateInfo = new QCouponDateInfo("couponDateInfo");

    public final EnumPath<com.chainz.coupon.shared.objects.CouponDateType> dateType = createEnum("dateType", com.chainz.coupon.shared.objects.CouponDateType.class);

    public final NumberPath<Integer> fixedBeginTerm = createNumber("fixedBeginTerm", Integer.class);

    public final NumberPath<Integer> fixedTerm = createNumber("fixedTerm", Integer.class);

    public final DatePath<java.time.LocalDate> timeRangeEnd = createDate("timeRangeEnd", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> timeRangeStart = createDate("timeRangeStart", java.time.LocalDate.class);

    public QCouponDateInfo(String variable) {
        super(CouponDateInfo.class, forVariable(variable));
    }

    public QCouponDateInfo(Path<? extends CouponDateInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponDateInfo(PathMetadata metadata) {
        super(CouponDateInfo.class, metadata);
    }

}

