package com.taipan.userbot.router;

import com.taipan.userbot.bot.BotCore;
import com.taipan.userbot.middleware.UpdateInterceptor;
import com.taipan.userbot.handler.CallbackQueryHandler;
import com.taipan.userbot.handler.CommandHandler;
import com.taipan.userbot.handler.DocumentHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateRouter {

    private final UpdateInterceptor interceptor;
    private final CommandHandler commandHandler;
    private final DocumentHandler documentHandler;
    private final CallbackQueryHandler callbackHandler;

    public UpdateRouter(UpdateInterceptor interceptor, CommandHandler commandHandler,
                        DocumentHandler documentHandler, CallbackQueryHandler callbackHandler) {
        this.interceptor = interceptor;
        this.commandHandler = commandHandler;
        this.documentHandler = documentHandler;
        this.callbackHandler = callbackHandler;
    }

    public void route(Update update, BotCore bot) {
        // 0️⃣ Сначала прогоним через interceptor
        interceptor.intercept(update);

        // 1️⃣ Маршрутизация
        if (update.hasMessage() && update.getMessage().hasText()) {
            commandHandler.handle(update, bot);
        } else if (update.hasMessage() && update.getMessage().hasDocument()) {
            documentHandler.handle(update, bot);
        } else if (update.hasCallbackQuery()) {
            callbackHandler.handle(update, bot);
        }
    }
}
