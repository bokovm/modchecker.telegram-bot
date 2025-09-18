package com.taipan.userbot;

import com.taipan.shared.service.BotManagerService;
import com.taipan.userbot.config.TelegramBotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UserBotApplication extends TelegramLongPollingBot {
    private final String BOT_ID = "user-bot";
    private final TelegramBotConfig config;
    private final BotManagerService botManager;

    public UserBotApplication(TelegramBotConfig config, BotManagerService botManager) {
        super(config.getToken());
        this.config = config;
        this.botManager = botManager;
        botManager.registerBot(BOT_ID, this);
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Обработка пользовательских сообщений
        // Эта логика должна быть в UpdateRouter
        System.out.println("Получено сообщение в user-bot: " + update.getMessage().getText());
    }
}