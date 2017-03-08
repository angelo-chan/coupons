package com.chainz.coupon.core.repository.common;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * QueryDsl join fetch capable repository.
 *
 * @param <T> Entity class.
 * @param <ID> ID.
 */
@NoRepositoryBean
public interface JoinFetchCapableQueryDslJpaRepository<T, ID extends Serializable>
    extends JpaRepository<T, ID>, QueryDslPredicateExecutor<T> {

  /**
   * Find one entity.
   *
   * @param predicate predicate.
   * @param joinDescriptors join descriptor.
   * @return entity.
   */
  T findOne(Predicate predicate, JoinDescriptor... joinDescriptors);

  /**
   * Find paginated entities.
   *
   * @param predicate predicate.
   * @param pageable pagination.
   * @param joinDescriptors join descriptor.
   * @return paginated entities.
   */
  Page<T> findAll(Predicate predicate, Pageable pageable, JoinDescriptor... joinDescriptors);

  /**
   * Find entities.
   *
   * @param predicate predicate.
   * @param joinDescriptors join descriptor.
   * @return entities.
   */
  List<T> findAll(Predicate predicate, JoinDescriptor... joinDescriptors);

  /**
   * Find entities.
   *
   * @param predicate predicate.
   * @param sort sort.
   * @param joinDescriptors join descriptor.
   * @return entities.
   */
  List<T> findAll(Predicate predicate, Sort sort, JoinDescriptor... joinDescriptors);
}
