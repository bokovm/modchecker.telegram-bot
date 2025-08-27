package com.taipan.adminbot.command;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface AdminCommand {
    String getCommandName();
    void execute(Message message, AbsSender bot);
}