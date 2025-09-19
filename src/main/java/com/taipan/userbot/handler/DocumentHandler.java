package com.taipan.userbot.handler;

import com.taipan.userbot.bot.BotCore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@PropertySource("classpath:UserBotHandlerMessages/document_handler.properties")
public class DocumentHandler {

    @Value("${check.message}")
    private String checkMessage;

    public void handle(Update update, BotCore bot) {
        String fileName = update.getMessage().getDocument().getFileName();
        long chatId = update.getMessage().getChatId();

        String text = checkMessage.replace("{0}", fileName);

        SendMessage response = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .build();

        try {
            bot.execute(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}