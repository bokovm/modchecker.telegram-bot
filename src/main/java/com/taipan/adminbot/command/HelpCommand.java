package com.taipan.adminbot.command;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Qualifier("HelpCommand")
public class HelpCommand implements AdminCommand {

    @Override
    public String getCommandName() {
        return "/help";
    }

    @Override
    public void execute(Message message, AbsSender bot) {
        String helpText = "📚 Команды админ-бота:\n\n" +
                "/sendmessage [@username|all] [bot-name] text - Отправить сообщение\n" +
                "/admin [add|remove] @username - Управление администраторами\n" +
                "/bots - Список зарегистрированных ботов\n" +
                "/help - Помощь";

        SendMessage response = SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(helpText)
                .build();

        try {
            bot.execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}