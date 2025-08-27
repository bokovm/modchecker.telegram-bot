package com.taipan.userbot.service;

import com.taipan.userbot.bot.BotCore;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class CallbackQueryHandler {

    public void handle(Update update, BotCore bot) {
        String data = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        // Пока просто отвечаем, но можно интегрировать с BotService
        SendMessage response = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Вы нажали кнопку: `" + data + "`")
                .parseMode("Markdown")
                .build();

        try {
            bot.execute(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
