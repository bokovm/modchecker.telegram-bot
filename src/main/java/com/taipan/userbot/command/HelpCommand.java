package com.taipan.userbot.command;

import org.springframework.stereotype.Component;

@Component("userHelpCommand")
public class HelpCommand extends AbstractBotCommand {

    @Override
    public String getCommandName() {
        return "/help";
    }

    @Override
    protected String getText(long chatId) {
        return "📚 Помощь по боту:\n\n"
                + "/start - Начать работу\n"
                + "/help - Получить помощь\n"
                + "/check - Проверить моды";
    }
}
