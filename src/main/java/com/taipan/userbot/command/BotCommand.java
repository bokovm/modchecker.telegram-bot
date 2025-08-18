package com.taipan.modchecker.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotCommand {
    String getCommandName();
    SendMessage execute(long chatId);
}
