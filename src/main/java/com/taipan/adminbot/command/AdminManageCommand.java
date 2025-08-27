package com.taipan.adminbot.command;

import com.taipan.adminbot.AdminBotApplication;
import com.taipan.adminbot.service.AdminService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class AdminManageCommand {
    private final AdminService adminService;

    public AdminManageCommand(AdminService adminService) {
        this.adminService = adminService;
    }

    public void handle(Message message, AdminBotApplication adminBot) {
        String[] args = message.getText().split(" ", 3);
        Long senderId = message.getFrom().getId();

        // Только существующие администраторы могут добавлять/удалять других
        if (!adminService.isAdmin(senderId)) {
            sendResponse(adminBot, message.getChatId(), "❌ Недостаточно прав.");
            return;
        }

        if (args.length < 2) {
            sendResponse(adminBot, message.getChatId(), "❌ Формат: /admin [add|remove] @username");
            return;
        }

        String action = args[1];
        String username = args.length > 2 ? args[2] : "";

        if ("add".equalsIgnoreCase(action)) {
            // Логика добавления администратора
            sendResponse(adminBot, message.getChatId(), "✅ Команда добавления администратора");
        } else if ("remove".equalsIgnoreCase(action)) {
            // Логика удаления администратора
            sendResponse(adminBot, message.getChatId(), "✅ Команда удаления администратора");
        } else {
            sendResponse(adminBot, message.getChatId(), "❌ Неизвестное действие: " + action);
        }
    }

    private void sendResponse(AdminBotApplication adminBot, Long chatId, String text) {
        try {
            adminBot.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка отправки ответа: " + e.getMessage());
        }
    }
}