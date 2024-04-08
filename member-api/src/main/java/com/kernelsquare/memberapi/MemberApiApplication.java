package com.kernelsquare.memberapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
	"com.kernelsquare.core",
	"com.kernelsquare.memberapi",
	"com.kernelsquare.domainmysql",
	"com.kernelsquare.domainmongodb",
	"com.kernelsquare.domainredis",
	"com.kernelsquare.domainkafka",
	"com.kernelsquare.domains3"})
public class MemberApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(MemberApiApplication.class, args);
	}

}
