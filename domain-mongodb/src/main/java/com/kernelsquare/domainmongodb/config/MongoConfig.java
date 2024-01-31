package com.kernelsquare.domainmongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("com.kernelsquare.domainmongodb.domain")
public class MongoConfig {
}
