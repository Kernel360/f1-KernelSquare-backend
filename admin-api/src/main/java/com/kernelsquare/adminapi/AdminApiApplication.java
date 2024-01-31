package com.kernelsquare.adminapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kernelsquare.core",
	"com.kernelsquare.adminapi", "com.kernelsquare.domainmysql"})
public class AdminApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApiApplication.class, args);
	}

}
