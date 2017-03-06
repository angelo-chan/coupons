package com.chainz.coupon.core.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/** Redis configuration. */
@SuppressWarnings({"unchecked"})
@Configuration
public class RedisConfiguration {

  /**
   * Initialize coupon grant redis template.
   *
   * @param factory redis connection factory.
   * @return redis template.
   */
  @Bean
  public RedisTemplate<String, CouponGrant> couponGrantRedisTemplate(
      RedisConnectionFactory factory) {
    RedisTemplate<String, CouponGrant> redisTemplate = getRedisTemplate(factory);
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(CouponGrant.class));
    return redisTemplate;
  }

  /**
   * Get redis template.
   *
   * @param factory redis connection factory.
   * @return redis template.
   */
  private RedisTemplate getRedisTemplate(RedisConnectionFactory factory) {
    RedisTemplate redisTemplate = new RedisTemplate();
    redisTemplate.setConnectionFactory(factory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    return redisTemplate;
  }
}
