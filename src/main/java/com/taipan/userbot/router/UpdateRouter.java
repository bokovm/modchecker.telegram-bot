package com.taipan.userbot.router;

import com.taipan.userbot.bot.BotCore;
import com.taipan.userbot.model.MessageHistory;
import com.taipan.userbot.model.UserEntity;
import com.taipan.userbot.service.MessageHistoryService;
import com.taipan.userbot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateRouter {
    private final UserService userService;
    private final MessageHistoryService messageHistoryService;

    public UpdateRouter(UserService userService, MessageHistoryService messageHistoryService) {
        this.userService = userService;
        this.messageHistoryService = messageHistoryService;
    }

    public void route(Update update, BotCore bot) {
        if (update.hasMessage()) {
            org.telegram.telegrambots.meta.api.objects.User tgUser = update.getMessage().getFrom();
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Сохраняем пользователя
            UserEntity user = userService.getOrCreateUser(tgUser);
            System.out.println("👤 User saved: " + user.getUsername());

            // Сохраняем историю сообщений
            MessageHistory message = new MessageHistory();
            message.setUser(user);
            message.setMessageText(messageText);
            messageHistoryService.saveMessage(message);
            System.out.println("💬 Message saved: " + messageText);

            // Обработка сообщения
            handleMessage(update, bot);
        }
    }

    private void handleMessage(Update update, BotCore bot) {
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