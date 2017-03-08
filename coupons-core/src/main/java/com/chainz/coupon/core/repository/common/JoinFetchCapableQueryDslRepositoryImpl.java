package com.chainz.coupon.core.repository.common;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * QueryDsl join fetch capable repository implementation.
 *
 * @param <T> entity type.
 * @param <ID> ID.
 */
@SuppressWarnings({"unchecked", "ClassTypeParameterName"})
public class JoinFetchCapableQueryDslRepositoryImpl<T, ID extends Serializable>
    extends QueryDslJpaRepository<T, ID> implements JoinFetchCapableQueryDslJpaRepository<T, ID> {

  private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER =
      SimpleEntityPathResolver.INSTANCE;

  private final EntityPath<T> path;
  private final PathBuilder<T> builder;
  private final Querydsl querydsl;

  /**
   * Creates a new {@link JoinFetchCapableQueryDslRepositoryImpl} from the given domain class and
   * {@link EntityManager}. This will use the {@link SimpleEntityPathResolver} to translate the
   * given domain class into an {@link EntityPath}.
   *
   * @param entityInformation must not be {@literal null}.
   * @param entityManager must not be {@literal null}.
   */
  public JoinFetchCapableQueryDslRepositoryImpl(
      JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
    this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
  }

  /**
   * Creates a new {@link JoinFetchCapableQueryDslRepositoryImpl} from the given domain class and
   * {@link EntityManager} and uses the given {@link EntityPathResolver} to translate the domain
   * class into an {@link EntityPath}.
   *
   * @param entityInformation must not be {@literal null}.
   * @param entityManager must not be {@literal null}.
   * @param resolver must not be {@literal null}.
   */
  public JoinFetchCapableQueryDslRepositoryImpl(
      JpaEntityInformation<T, ID> entityInformation,
      EntityManager entityManager,
      EntityPathResolver resolver) {

    super(entityInformation, entityManager);

    this.path = resolver.createPath(entityInformation.getJavaType());
    this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
    this.querydsl = new Querydsl(entityManager, builder);
  }

  @Override
  public T findOne(Predicate predicate, JoinDescriptor... joinDescriptors) {
    return createFetchQuery(predicate, joinDescriptors).select(path).fetchOne();
  }

  @Override
  public List<T> findAll(Predicate predicate, JoinDescriptor... joinDescriptors) {
    return createFetchQuery(predicate, joinDescriptors).select(path).fetch();
  }

  @Override
  public List<T> findAll(Predicate predicate, Sort sort, JoinDescriptor... joinDescriptors) {
    return executeSorted(createFetchQuery(predicate).select(path), sort);
  }

  @Override
  public Page<T> findAll(
      Predicate predicate, Pageable pageable, JoinDescriptor... joinDescriptors) {
    final JPQLQuery<?> countQuery = createCountQuery(predicate);
    JPQLQuery query =
        querydsl.applyPagination(
            pageable, createFetchQuery(predicate, joinDescriptors).select(path));

    return PageableExecutionUtils.getPage(query.fetch(), pageable, () -> countQuery.fetchCount());
  }

  /**
   * Create fetch query.
   *
   * @param predicate predicate.
   * @param joinDescriptors join descriptor.
   * @return JPQLQuery.
   */
  protected JPQLQuery<?> createFetchQuery(Predicate predicate, JoinDescriptor... joinDescriptors) {
    JPQLQuery query = createQuery(predicate);
    for (JoinDescriptor joinDescriptor : joinDescriptors) {
      join(joinDescriptor, query);
    }
    return query;
  }

  /**
   * Join.
   *
   * @param joinDescriptor join descriptor.
   * @param query query.
   */
  private void join(JoinDescriptor joinDescriptor, JPQLQuery query) {
    switch (joinDescriptor.getType()) {
      case INNERJOIN:
        query.innerJoin(joinDescriptor.getPath()).fetchJoin();
        break;
      case JOIN:
        query.join(joinDescriptor.getPath()).fetchJoin();
        break;
      case LEFTJOIN:
        query.leftJoin(joinDescriptor.getPath()).fetchJoin();
        break;
      case RIGHTJOIN:
        query.rightJoin(joinDescriptor.getPath()).fetchJoin();
        break;
      case DEFAULT:
      case FULLJOIN:
        throw new IllegalArgumentException("cross join not supported");
      default:
        throw new IllegalArgumentException("cross join not supported");
    }
  }

  /**
   * Executes the given {@link JPQLQuery} after applying the given {@link Sort}.
   *
   * @param query must not be {@literal null}.
   * @param sort must not be {@literal null}.
   * @return  entity list.
   */
  private List<T> executeSorted(JPQLQuery<T> query, Sort sort) {
    return querydsl.applySorting(sort, query).fetch();
  }
}
