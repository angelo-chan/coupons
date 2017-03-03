package com.chainz.coupon.core.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = -1454279974L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final StringPath brandName = createString("brandName");

    public final BooleanPath canShare = createBoolean("canShare");

    public final NumberPath<Integer> circulation = createNumber("circulation", Integer.class);

    public final StringPath color = createString("color");

    public final DateTimePath<java.time.ZonedDateTime> createdAt = createDateTime("createdAt", java.time.ZonedDateTime.class);

    public final QCouponDateInfo dateInfo;

    public final StringPath description = createString("description");

    public final QCouponExtension extension;

    public final NumberPath<Integer> getLimit = createNumber("getLimit", Integer.class);

    public final StringPath id = createString("id");

    public final QCouponIssuer issuer;

    public final StringPath notice = createString("notice");

    public final StringPath servicePhone = createString("servicePhone");

    public final NumberPath<Integer> sku = createNumber("sku", Integer.class);

    public final EnumPath<com.chainz.coupon.shared.objects.CouponStatus> status = createEnum("status", com.chainz.coupon.shared.objects.CouponStatus.class);

    public final SetPath<String, StringPath> stores = this.<String, StringPath>createSet("stores", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath subtitle = createString("subtitle");

    public final EnumPath<com.chainz.coupon.shared.objects.CouponTarget> target = createEnum("target", com.chainz.coupon.shared.objects.CouponTarget.class);

    public final StringPath title = createString("title");

    public final EnumPath<com.chainz.coupon.shared.objects.CouponType> type = createEnum("type", com.chainz.coupon.shared.objects.CouponType.class);

    public final DateTimePath<java.time.ZonedDateTime> updatedAt = createDateTime("updatedAt", java.time.ZonedDateTime.class);

    public final NumberPath<Float> value = createNumber("value", Float.class);

    public QCoupon(String variable) {
        this(Coupon.class, forVariable(variable), INITS);
    }

    public QCoupon(Path<? extends Coupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoupon(PathMetadata metadata, PathInits inits) {
        this(Coupon.class, metadata, inits);
    }

    public QCoupon(Class<? extends Coupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dateInfo = inits.isInitialized("dateInfo") ? new QCouponDateInfo(forProperty("dateInfo")) : null;
        this.extension = inits.isInitialized("extension") ? new QCouponExtension(forProperty("extension")) : null;
        this.issuer = inits.isInitialized("issuer") ? new QCouponIssuer(forProperty("issuer")) : null;
    }

}

