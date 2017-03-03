package com.chainz.coupon.core.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponExtension is a Querydsl query type for CouponExtension
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QCouponExtension extends BeanPath<CouponExtension> {

    private static final long serialVersionUID = 1336434309L;

    public static final QCouponExtension couponExtension = new QCouponExtension("couponExtension");

    public final StringPath customLinkName = createString("customLinkName");

    public final StringPath customLinkTitle = createString("customLinkTitle");

    public final StringPath customLinkUrl = createString("customLinkUrl");

    public QCouponExtension(String variable) {
        super(CouponExtension.class, forVariable(variable));
    }

    public QCouponExtension(Path<? extends CouponExtension> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponExtension(PathMetadata metadata) {
        super(CouponExtension.class, metadata);
    }

}

