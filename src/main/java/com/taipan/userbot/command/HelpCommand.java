package com.taipan.userbot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HelpCommand implements BotCommand {

    @Override
    public String getCommandName() {
        return "/help";
    }

    @Override
    public SendMessage execute(long chatId) {
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("📚 Помощь по боту:\n\n"
                        + "/start - Начать работу\n"
                        + "/help - Получить помощь\n"
                        + "/check - Проверить моды")
                .build();
    }
}