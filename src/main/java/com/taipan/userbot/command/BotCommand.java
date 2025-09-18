package com.taipan.userbot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotCommand {
    String getCommandName();
    SendMessage execute(long chatId);
}
