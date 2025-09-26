package com.taipan.adminbot.command;

import com.taipan.adminbot.service.AdminManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@Qualifier("AdminManageCommand")
public class AdminManageCommand implements AdminCommand {

    private final AdminManageService adminManageService;

    public AdminManageCommand(AdminManageService adminManageService) {
        this.adminManageService = adminManageService;
    }

    @Override
    public String getCommandName() {
        return "/admin";
    }

    @Override
    public void execute(Message message, AbsSender bot) {
        String[] args = message.getText().split(" ", 3);
        Long senderId = message.getFrom().getId();

        if (!adminManageService.isAdmin(senderId)) {
            sendResponse(bot, message.getChatId(), "❌ Недостаточно прав.");
            return;
        }

        if (args.length < 2) {
            sendResponse(bot, message.getChatId(), "❌ Формат: /admin [add|remove] @username");
            return;
        }

        String action = args[1];
        String username = args.length > 2 ? args[2] : "";

        String response;
        if ("add".equalsIgnoreCase(action)) {
            response = adminManageService.addAdmin(username);
        } else if ("remove".equalsIgnoreCase(action)) {
            response = adminManageService.removeAdmin(username);
        } else {
            response = "❌ Неизвестное действие: " + action;
        }

        sendResponse(bot, message.getChatId(), response);
    }

    private void sendResponse(AbsSender bot, Long chatId, String text) {
        SendMessage response = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        try {
            bot.execute(response);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки ответа: {} ", e.getMessage(), e);
        }
    }
}
