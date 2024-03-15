package com.kernelsquare.memberapi.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

@Configuration
public class RedisConfig {
	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setValueSerializer(jsonRedisSerializer());
		redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setHashValueSerializer(jsonRedisSerializer());
		return redisTemplate;
	}

	@Bean
	GenericJackson2JsonRedisSerializer jsonRedisSerializer() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return new GenericJackson2JsonRedisSerializer(objectMapper);
	}
}
