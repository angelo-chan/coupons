package com.chainz.coupon.core.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponIssuer is a Querydsl query type for CouponIssuer
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QCouponIssuer extends BeanPath<CouponIssuer> {

    private static final long serialVersionUID = -1105279437L;

    public static final QCouponIssuer couponIssuer = new QCouponIssuer("couponIssuer");

    public final StringPath issuerId = createString("issuerId");

    public final EnumPath<com.chainz.coupon.shared.objects.CouponIssuerType> issuerType = createEnum("issuerType", com.chainz.coupon.shared.objects.CouponIssuerType.class);

    public QCouponIssuer(String variable) {
        super(CouponIssuer.class, forVariable(variable));
    }

    public QCouponIssuer(Path<? extends CouponIssuer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponIssuer(PathMetadata metadata) {
        super(CouponIssuer.class, metadata);
    }

}

