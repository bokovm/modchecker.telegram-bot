package com.taipan.userbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot.user")
@Getter
@Setter
public class TelegramBotConfig {
    private String token;
    private String username;
}