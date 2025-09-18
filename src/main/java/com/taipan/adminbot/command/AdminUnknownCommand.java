package com.taipan.adminbot.command;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
            e.printStackTrace();
        }
    }
}