package com.taipan.userbot.handler;

import com.taipan.userbot.bot.BotCore;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class DocumentHandler {

    public void handle(Update update, BotCore bot) {
        String fileName = update.getMessage().getDocument().getFileName();
        long chatId = update.getMessage().getChatId();

        SendMessage response = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("📄 Файл получен: " + fileName + "\n\nФункционал обработки модов в разработке!")
                .build();

        try {
            bot.execute(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}