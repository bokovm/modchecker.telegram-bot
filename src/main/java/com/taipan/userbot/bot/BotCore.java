package com.taipan.modchecker.bot;

import com.taipan.modchecker.config.TelegramBotConfig;
import com.taipan.modchecker.router.UpdateRouter;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Primary  // Добавляем эту аннотацию
public class BotCore extends TelegramLongPollingBot {

    private final TelegramBotConfig config;
    private final UpdateRouter updateRouter;

    public BotCore(TelegramBotConfig config, UpdateRouter updateRouter) {
        this.config = config;
        this.updateRouter = updateRouter;
    }

    @PostConstruct
    public void init() {
        try {
            // Очистка вебхука при старте
            this.clearWebhook();

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("✅ Бот запущен!");
        } catch (TelegramApiException e) {
            if (e instanceof TelegramApiRequestException apiReqEx) {
                if (!apiReqEx.getMessage().contains("404") && !apiReqEx.getMessage().contains("Conflict")) {
                    e.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            updateRouter.route(update, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}