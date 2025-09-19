package com.taipan.userbot.handler;

import com.taipan.userbot.bot.BotCore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@PropertySource("classpath:callback_query.properties")
@Service
public class CallbackQueryHandler {

    @Value("${button.clicked:Вы нажали кнопку}")
    private String buttonMessage;

    public void handle(Update update, BotCore bot) {
        String data = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        // Если нужно вставить data в сообщение, можно использовать replace или String.format
        String text = buttonMessage.replace("{0}", data);
        // или: String text = String.format(buttonMessage, data);

        SendMessage response = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .parseMode("Markdown")
                .build();

        try {
            bot.execute(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
