package com.kernelsquare.memberapi.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;

@Configuration
@EnableFeignClients(basePackages = "com.kernelsquare.memberapi")
public class FeignConfig {
	@Bean
	public Client feignClient() {
		return new Client.Default(null, null);
	}
}
