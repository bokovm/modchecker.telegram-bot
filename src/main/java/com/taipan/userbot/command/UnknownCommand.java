package com.taipan.userbot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class UnknownCommand implements BotCommand {

    @Override
    public String getCommandName() {
        return "unknown";
    }

    @Override
    public SendMessage execute(long chatId) {
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("❓ Извините, я не понимаю эту команду. Попробуйте /help.")
                .build();
    }
}