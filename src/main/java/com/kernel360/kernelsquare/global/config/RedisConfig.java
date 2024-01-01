package com.kernel360.kernelsquare.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	@Bean
	public RedisTemplate<Long, String> redisTemplate() {
		RedisTemplate<Long, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	}
}
