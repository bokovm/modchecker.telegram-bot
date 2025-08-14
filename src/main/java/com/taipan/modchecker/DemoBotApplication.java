package com.taipan.modchecker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class DemoBotApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        SimpleBot bot = new SimpleBot();

        // Очищаем вебхук один раз при старте (чтобы точно работал LongPolling)
        try {
            bot.clearWebhook();
        } catch (TelegramApiRequestException e) {
            if (!e.getMessage().contains("404")) {
                throw e;
            }
        }

        botsApi.registerBot(bot);
        System.out.println("✅ Бот запущен!");
    }

    static class SimpleBot extends TelegramLongPollingBot {

        // ⚠️ Вставь сюда свои данные
        private static final String BOT_TOKEN = "8117311229:AAG7A2gAVruIzOmJp_mcSFOwTYb7RUWw1cA";
        private static final String BOT_USERNAME = "modchecker_bot";

        @Override
        public String getBotUsername() {
            return BOT_USERNAME;
        }

        @Override
        public String getBotToken() {
            return BOT_TOKEN;
        }

        @Override
        public void onUpdateReceived(Update update) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String chatId = update.getMessage().getChatId().toString();
                String text = update.getMessage().getText();

                SendMessage message = new SendMessage(chatId, "Привет! Ты написал: " + text);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
