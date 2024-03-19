package com.kernelsquare.alertapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.kernelsquare.core",
    "com.kernelsquare.alertapi",
    "com.kernelsquare.domainmysql",
    "com.kernelsquare.domainmongodb"})
public class AlertApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertApiApplication.class, args);
    }

}
