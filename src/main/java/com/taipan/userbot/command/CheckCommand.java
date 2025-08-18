package com.taipan.modchecker.command;

import org.springframework.stereotype.Component;

@Component
public class CheckCommand extends AbstractBotCommand {
    @Override
    public String getCommandName() {
        return "/check";
    }

    @Override
    protected String getText(long chatId) {
        return "Загрузите файл модпака в формате .zip";
    }
}