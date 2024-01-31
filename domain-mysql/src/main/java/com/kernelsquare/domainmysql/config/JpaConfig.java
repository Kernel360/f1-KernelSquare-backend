package com.kernelsquare.domainmysql.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.kernelsquare.domainmysql.domain")
@EntityScan("com.kernelsquare.domainmysql.domain")
public class JpaConfig {
}
