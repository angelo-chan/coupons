package com.chainz.coupon.core.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/** Created by AngeloChen on 08/03/2017. */
public class JoinFetchCapableQueryDslJpaRepositoryFactoryBean<
        R extends JpaRepository<T, I>, T, I extends Serializable>
    extends JpaRepositoryFactoryBean<R, T, I> {

  /**
   * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
   *
   * @param repositoryInterface must not be {@literal null}.
   */
  public JoinFetchCapableQueryDslJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new JoinFetchCapableQueryDslJpaRepositoryFactory(entityManager);
  }

  /** Custom QueryDsl repository factory. */
  private static class JoinFetchCapableQueryDslJpaRepositoryFactory extends JpaRepositoryFactory {

    /**
     * Constructor.
     *
     * @param entityManager entity manager.
     */
    public JoinFetchCapableQueryDslJpaRepositoryFactory(EntityManager entityManager) {
      super(entityManager);
    }

    @Override
    protected SimpleJpaRepository<?, ?> getTargetRepository(
        RepositoryInformation information, EntityManager entityManager) {

      JpaEntityInformation<?, Serializable> entityInformation =
          getEntityInformation(information.getDomainType());

      return new JoinFetchCapableQueryDslRepositoryImpl<>(entityInformation, entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return JoinFetchCapableQueryDslJpaRepository.class;
    }
  }
}
