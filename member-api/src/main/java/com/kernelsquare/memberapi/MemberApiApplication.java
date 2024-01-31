package com.kernelsquare.memberapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
	"com.kernelsquare.core",
	"com.kernelsquare.memberapi",
	"com.kernelsquare.domainmysql",
	"com.kernelsquare.domainmongodb"})
public class MemberApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(MemberApiApplication.class, args);
	}

}
