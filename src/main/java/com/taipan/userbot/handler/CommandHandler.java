package com.taipan.userbot.handler;

import com.taipan.userbot.bot.BotCore;
import com.taipan.userbot.service.BotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
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
            log.error("Ошибка обработки сообщения от пользователя {}, дополнтельные данные - {}", chatId, commandText, e);
        }
    }
}
