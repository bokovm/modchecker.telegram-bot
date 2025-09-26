package com.taipan.adminbot.command;

import com.taipan.shared.service.BotManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@Qualifier("ListBotsCommand")
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
        long chatId = message.getChatId();
        Long userId = message.getFrom().getId();

        log.debug("Пользователь {} запросил список ботов в чате {}", userId, chatId);

        try {
            String botsList = "🤖 Зарегистрированные боты:\n\n" +
                    botManagerService.getRegisteredBotsList();

            SendMessage response = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text(botsList)
                    .build();

            bot.execute(response);
            log.info("Список ботов успешно отправлен пользователю {} в чат {}", userId, chatId);

        } catch (TelegramApiException e) {
            log.error("Не удалось отправить список ботов пользователю {} в чат {}: {}",
                    userId, chatId, e.getMessage(), e);

            sendErrorMessage(bot, chatId, "Не удалось отправить список ботов");
        } catch (Exception e) {
            log.error("Неожиданная ошибка при получении списка ботов для пользователя {}: {}",
                    userId, e.getMessage(), e);
            sendErrorMessage(bot, chatId, "Произошла внутренняя ошибка");
        }
    }

    private void sendErrorMessage(AbsSender bot, long chatId, String errorText) {
        try {
            SendMessage errorMsg = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text("⚠️ " + errorText)
                    .build();
            bot.execute(errorMsg);
        } catch (TelegramApiException ex) {
            log.error("Критическая ошибка: не удалось отправить сообщение об ошибке в чат {}", chatId, ex);
        }
    }
}