package com.kernel360.kernelsquare.global.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    @Bean
    public Flyway flywayBean() {
        Flyway flyway = Flyway.configure()
            .baselineOnMigrate(true)
            .dataSource(System.getenv("LOCAL_DB_URL")+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8",
                System.getenv("LOCAL_DB_HOST"), System.getenv("LOCAL_DB_PASSWORD"))
            .load();
        flyway.repair();
        flyway.migrate();
        return flyway;
    }
}
