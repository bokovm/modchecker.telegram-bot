package com.taipan.userbot.middleware;

import com.taipan.userbot.model.MessageHistory;
import com.taipan.userbot.model.UserEntity;
import com.taipan.userbot.service.MessageHistoryService;
import com.taipan.userbot.service.UserService;
import com.taipan.userbot.bot.BotCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UpdateInterceptor.class);

    private final UserService userService;
    private final MessageHistoryService historyService;

    public UpdateInterceptor(UserService userService, MessageHistoryService historyService) {
        this.userService = userService;
        this.historyService = historyService;
    }

    public void intercept(Update update) {
        try {
            // Сохраняем пользователя
            if (update.hasMessage() && update.getMessage().getFrom() != null) {
                UserEntity user = userService.getOrCreateUser(update.getMessage().getFrom());
                logger.debug("User processed: {}", user.getUsername());
            }

            // Сохраняем историю сообщений
            if (update.hasMessage() && update.getMessage().hasText()) {
                // Получаем или создаём пользователя
                UserEntity user = userService.getOrCreateUser(update.getMessage().getFrom());

                // Создаём запись истории сообщений
                MessageHistory history = new MessageHistory();
                history.setUser(user); // теперь объект UserEntity
                history.setMessageText(update.getMessage().getText());
                // timestamp заполнится автоматически через initialization или @PrePersist

                // Сохраняем запись в БД
                historyService.saveMessage(history);
                logger.debug("Message saved: {}", update.getMessage().getText());
            }


        } catch (Exception e) {
            logger.error("Error in UpdateInterceptor", e);
        }
    }
}
