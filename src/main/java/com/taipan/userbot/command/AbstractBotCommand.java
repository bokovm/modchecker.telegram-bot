package com.taipan.userbot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;

public abstract class AbstractBotCommand implements BotCommand {

    public abstract String getCommandName();
    protected abstract String getText(long chatId);

    protected List<List<InlineKeyboardButton>> getButtons() {
        return Collections.emptyList();
    }

    @Override
    public SendMessage execute(long chatId) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(getButtons());

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(getText(chatId))
                .parseMode("Markdown")
                .replyMarkup(keyboardMarkup)
                .build();
    }
}
