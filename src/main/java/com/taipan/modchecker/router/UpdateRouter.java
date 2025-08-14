package com.taipan.modchecker.router;

import com.taipan.modchecker.bot.BotCore;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateRouter {

    public void route(Update update, BotCore bot) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                // Временная реализация - будет заменена после добавления CommandHandler
                handleTextMessage(update, bot);
            } else if (update.getMessage().hasDocument()) {
                handleDocument(update, bot);
            }
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update, bot);
        }
    }

    private void handleTextMessage(Update update, BotCore bot) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        String responseText = "Получено сообщение: " + text;
        if (text.startsWith("/")) {
            responseText = "Команда " + text + " получена. Обработчик команд будет добавлен позже.";
        }

        sendResponse(chatId, responseText, bot);
    }

    private void handleDocument(Update update, BotCore bot) {
        String fileName = update.getMessage().getDocument().getFileName();
        long chatId = update.getMessage().getChatId();
        sendResponse(chatId, "📄 Получен файл: " + fileName, bot);
    }

    private void handleCallbackQuery(Update update, BotCore bot) {
        String data = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        sendResponse(chatId, "Вы нажали кнопку: " + data, bot);
    }

    private void sendResponse(long chatId, String text, BotCore bot) {
        try {
            bot.execute(org.telegram.telegrambots.meta.api.methods.send.SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text(text)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}