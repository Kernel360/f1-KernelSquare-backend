package com.kernel360.kernelsquare.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernel360.kernelsquare.domain.auth.entity.RefreshToken;

@Configuration
public class RedisConfig {
	@Value("${spring.redis.serialization.class-property-type-name}")
	String classPropertyTypeName;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}

	@Bean
	public RedisTemplate<Long, RefreshToken> redisTemplate() {
		RedisTemplate<Long, RefreshToken> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setValueSerializer(jsonRedisSerializer());
		return redisTemplate;
	}

	@Bean
	GenericJackson2JsonRedisSerializer jsonRedisSerializer() {
		GenericJackson2JsonRedisSerializer jsonRedisSerializer =
			new GenericJackson2JsonRedisSerializer(classPropertyTypeName);

		jsonRedisSerializer.configure(objectMapper -> objectMapper
			.registerModule(new JavaTimeModule()));

		return jsonRedisSerializer;
	}
	// @Bean
	// public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
	// 	PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
	// 		.builder()
	// 		.allowIfSubType(Object.class)
	// 		.build();
	//
	// 	ObjectMapper objectMapper = new ObjectMapper();
	// 	objectMapper.registerModule(new JavaTimeModule());
	// 	objectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);
	// 	GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
	// }
}
