package com.kernel360.kernelsquare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KernelSquareApplication {
	public static void main(String[] args) {
		SpringApplication.run(KernelSquareApplication.class, args);
	}

}
