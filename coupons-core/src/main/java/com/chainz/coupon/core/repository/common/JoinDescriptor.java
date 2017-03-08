package com.chainz.coupon.core.repository.common;

import com.querydsl.core.JoinType;
import com.querydsl.core.types.EntityPath;
import lombok.Getter;
import lombok.Setter;

/** QueryDsl join descriptor. */
@Getter
@Setter
public class JoinDescriptor {

  private final EntityPath path;

  private final JoinType type;

  /**
   * private constructor.
   *
   * @param path entity path.
   * @param type join type.
   */
  private JoinDescriptor(EntityPath path, JoinType type) {
    this.path = path;
    this.type = type;
  }

  /**
   * Inner join.
   *
   * @param path entity path.
   * @return join descriptor.
   */
  public static JoinDescriptor innerJoin(EntityPath path) {
    return new JoinDescriptor(path, JoinType.INNERJOIN);
  }

  /**
   * Join.
   *
   * @param path entity path.
   * @return join descriptor.
   */
  public static JoinDescriptor join(EntityPath path) {
    return new JoinDescriptor(path, JoinType.JOIN);
  }

  /**
   * Left join.
   *
   * @param path entity path.
   * @return join descriptor.
   */
  public static JoinDescriptor leftJoin(EntityPath path) {
    return new JoinDescriptor(path, JoinType.LEFTJOIN);
  }

  /**
   * Right join.
   *
   * @param path entity path.
   * @return join descriptor.
   */
  public static JoinDescriptor rightJoin(EntityPath path) {
    return new JoinDescriptor(path, JoinType.RIGHTJOIN);
  }
}
