package com.kernelsquare.domainmongodb.common.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableEncryptableProperties
public class TestJasyptConfig {
    @Value("${jasypt.password}")
    private String password;

    @Bean("jasyptEncryptorAES")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256"); // 알고리즘
        config.setKeyObtentionIterations("1000"); // 반복할 해싱 횟수
        config.setPoolSize("1"); // 인스턴스 pool
        config.setProviderName("SunJCE"); // 기본 암호화 제공자
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // 기본 salt 생성 클래스
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator"); // 랜덤 초기화 벡터
        config.setStringOutputType("base64"); //인코딩 방식
        encryptor.setConfig(config);
        return encryptor;
    }
}
