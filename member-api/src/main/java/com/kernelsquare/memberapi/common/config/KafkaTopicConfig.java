package com.kernelsquare.memberapi.common.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.url}")
    private String url;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        return new KafkaAdmin(config);
    }

    @Bean
    public NewTopic chatTopic() {
        return new NewTopic("chatting",1, (short)1);
    }

    @Bean
    public NewTopic alertTopic() {
        return new NewTopic("alert",1, (short)1);
    }
}
