package com.taipan.core.bot;

import com.taipan.shared.service.BotManagerService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
public abstract class BaseBot extends TelegramLongPollingBot {
    protected final String botId;
    protected final String botName;
    protected final BotManagerService botManagerService;

    public BaseBot(String token, String botId, String botName, BotManagerService botManagerService) {
        super(token);
        this.botId = botId;
        this.botName = botName;
        this.botManagerService = botManagerService;
    }

    @PostConstruct
    public void init() {
        log.info("🔄 [{}] Запускаю бота '{}'...", botId, botName);

        try {
            // Действия перед регистрацией (очистка вебхука и т.д.)
            preRegistration();

            // Регистрация в Telegram
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            log.info("✅ [{}] Бот '{}' зарегистрирован в Telegram", botId, botName);

            // Действия после регистрации (регистрация в BotManagerService и т.д.)
            postRegistration();

            log.info("✅ [{}] Бот '{}' успешно запущен!", botId, botName);

        } catch (Exception e) {
            handleInitError(e);
        }
    }

    /**
     * Действия перед регистрацией бота в Telegram
     * Переопределить в наследниках при необходимости
     */
    protected void preRegistration() throws Exception {
        // Базовая реализация - очистка вебхука
        try {
            this.clearWebhook();
            log.debug("🔄 [{}] Вебхук очищен", botId);
        } catch (Exception e) {
            if (shouldIgnoreWebhookError(e)) {
                log.debug("⚠️ [{}] Игнорируемая ошибка очистки вебхука: {}", botId, e.getMessage());
            } else {
                throw e;
            }
        }
    }

    /**
     * Действия после регистрации бота в Telegram
     * Переопределить в наследниках при необходимости
     */
    protected void postRegistration() throws Exception {
        // Базовая реализация пуста
    }

    /**
     * Обработка ошибок инициализации с детальной диагностикой
     */
    protected void handleInitError(Exception e) {
        String errorType = classifyError(e);

        log.error("❌ [{}] КРИТИЧЕСКАЯ ОШИБКА при запуске бота '{}'", botId, botName);
        log.error("📋 Тип ошибки: {}", errorType);
        log.error("🔧 Детали: {}", e.getMessage());
        log.error("📚 Stacktrace:", e);
    }

    /**
     * Классификация ошибок для лучшей диагностики
     */
    private String classifyError(Exception e) {
        if (e instanceof TelegramApiException) {
            String message = e.getMessage().toLowerCase();
            if (message.contains("invalid token")) return "НЕВЕРНЫЙ ТОКЕН";
            if (message.contains("conflict")) return "КОНФЛИКТ (бот уже запущен)";
            if (message.contains("404")) return "СЕТЕВАЯ ОШИБКА (404)";
            if (message.contains("webhook")) return "ОШИБКА ВЕБХУКА";
        }
        return "НЕИЗВЕСТНАЯ ОШИБКА: " + e.getClass().getSimpleName();
    }

    /**
     * Определяем, какие ошибки вебхука можно игнорировать
     */
    private boolean shouldIgnoreWebhookError(Exception e) {
        return e.getMessage() != null &&
                (e.getMessage().contains("404") ||
                        e.getMessage().contains("Conflict"));
    }

    // Геттеры для доступа к идентификаторам
    public String getBotId() {
        return botId;
    }

    public String getBotName() {
        return botName;
    }
}