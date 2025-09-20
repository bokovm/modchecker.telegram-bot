package com.taipan.userbot.bot;

import com.taipan.userbot.config.TelegramBotConfig;
import com.taipan.userbot.router.UpdateRouter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
@Primary
public class BotCore extends TelegramLongPollingBot {

    private final TelegramBotConfig config;
    private final UpdateRouter updateRouter;

    public BotCore(TelegramBotConfig config, UpdateRouter updateRouter) {
        super(config.getToken());
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
            log.info("Пользовательский бот запущен!");
        } catch (TelegramApiException e) {
            if (e instanceof TelegramApiRequestException apiReqEx) {
                if (apiReqEx.getMessage().contains("404") || apiReqEx.getMessage().contains("Conflict")) {
                    log.debug("Игнорируемая ОШИБКА при очистке вебхука {}", e.getMessage());
                }
            } else {
                log.error("Ошибка при регистрации бота {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("Неожиданная ошибка при инициализации бота {}", e.getMessage(), e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            updateRouter.route(update, this);
        } catch (Exception e) {
            log.error("Ошибка обработки update: {}", e.getMessage(), e);;
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