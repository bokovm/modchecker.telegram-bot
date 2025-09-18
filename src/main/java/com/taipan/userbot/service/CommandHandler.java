package com.taipan.userbot.service;

import com.taipan.userbot.bot.BotCore;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class CommandHandler {

    private final BotService botService;

    public CommandHandler(BotService botService) {
        this.botService = botService;
    }

    public void handle(Update update, BotCore bot) {
        String commandText = update.getMessage().getText().trim();
        long chatId = update.getMessage().getChatId();

        SendMessage response = botService.processCommand(commandText, chatId);

        try {
            bot.execute(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
