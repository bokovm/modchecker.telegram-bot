package com.taipan;

import com.taipan.adminbot.config.AdminBotConfig;
import com.taipan.userbot.config.TelegramBotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties({TelegramBotConfig.class, AdminBotConfig.class})
@EnableJpaRepositories(basePackages = {
        "com.taipan.userbot.repository",
        "com.taipan.adminbot.repository"
})

@EntityScan(basePackages = {
        "com.taipan.userbot.model",
        "com.taipan.adminbot.model"
})

@EnableAsync
public class MultiBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiBotApplication.class, args);
    }
}