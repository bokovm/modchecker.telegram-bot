package com.taipan.modchecker;

import com.taipan.modchecker.config.TelegramBotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties(TelegramBotConfig.class)
@EnableJpaRepositories(basePackages = "com.taipan.modchecker.repository")
public class DemoBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoBotApplication.class, args);
    }
}