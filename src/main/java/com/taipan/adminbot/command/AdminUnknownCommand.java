package com.taipan.adminbot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@Qualifier("AdminUnknownCommand")
public class AdminUnknownCommand implements AdminCommand {

    @Override
    public String getCommandName() {
        return "unknown";
    }

    @Override
    public void execute(Message message, AbsSender bot) {
        SendMessage response = SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("❓ Неизвестная команда. Используйте /help для справки.")
                .build();

        try {
            bot.execute(response);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения в чат {}: {}", message.getChatId(), e.getMessage(), e);
        }
    }
}