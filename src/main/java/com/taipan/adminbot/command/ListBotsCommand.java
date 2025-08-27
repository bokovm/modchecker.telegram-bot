package com.taipan.adminbot.command;

import com.taipan.shared.service.BotManagerService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ListBotsCommand implements AdminCommand {

    private final BotManagerService botManagerService;

    public ListBotsCommand(BotManagerService botManagerService) {
        this.botManagerService = botManagerService;
    }

    @Override
    public String getCommandName() {
        return "/bots";
    }

    @Override
    public void execute(Message message, AbsSender bot) {
        String botsList = "🤖 Зарегистрированные боты:\n\n" +
                botManagerService.getRegisteredBotsList();

        SendMessage response = SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(botsList)
                .build();

        try {
            bot.execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}