package com.taipan.modchecker.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class StartCommand extends AbstractBotCommand {

    @Override
    public String getCommandName() {
        return "/start";
    }

    @Override
    protected String getText(long chatId) {
        return "*👋 Привет!*\n\nЧтобы посмотреть список команд, нажми кнопку ниже.";
    }

    @Override
    protected List<List<InlineKeyboardButton>> getButtons() {
        InlineKeyboardButton helpButton = InlineKeyboardButton.builder()
                .text("Помощь")
                .callbackData("/help")
                .build();

        return List.of(List.of(helpButton));
    }
}
