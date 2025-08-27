package com.taipan.adminbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot.admin")
@Getter
@Setter
public class AdminBotConfig {
    private String token;
    private String username;
}